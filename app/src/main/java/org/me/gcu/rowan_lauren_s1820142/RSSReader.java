package org.me.gcu.rowan_lauren_s1820142;

import android.os.AsyncTask;
import android.util.Log;

import org.me.gcu.rowan_lauren_s1820142.Models.FeedItem;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

//Lauren Rowan S1820142

public class RSSReader {

    private String source;
    private String sourceType;

    private ArrayList<FeedItem> items = new ArrayList<>();

    public InputStream getInputStream(URL url){
        try {
            return url.openConnection().getInputStream();
        }catch (IOException exception){
            return null;
        }
    }

    public RSSReader (String source, RSSParsingDone runWhenDone, String sourceType){
        this.source = source;
        this.sourceType = sourceType;

        new BackgroundJob(runWhenDone).execute();

    }


    public class BackgroundJob extends AsyncTask<Integer, Integer, Exception> {

        Exception exception = null;
        RSSParsingDone parsingDone;

        public BackgroundJob(RSSParsingDone parsingDoneListener){
            parsingDone = parsingDoneListener;
        }

        @Override
        protected void onPreExecute() {
            //This method runs before the background task starts
            super.onPreExecute();
        }

        @Override
        protected Exception doInBackground(Integer... integers) {
            try {
                //Temporary variables to hold the values for the current iterating item in the rss feed
                String title = null;
                String description = null;
                float latitude = 0;
                float longitude = 0;
                Date pubDate = new Date();
                String type = sourceType;

                //Roadwork specific properties below:
                Date startDate = sourceType.equals("INCIDENT") ? null : new Date();
                Date endDate = sourceType.equals("INCIDENT") ? null : new Date();

                //URL object of the url
                URL url = new URL(source);

                //Create new instance of the xml pull parser factory object
                XmlPullParserFactory fac = XmlPullParserFactory.newInstance();

                //Don't need to support XML namespace support
                fac.setNamespaceAware(false);

                //Create a new pullparser object (p)
                XmlPullParser p = fac.newPullParser();

                //Set the input for the pullparser and set the encoding for the characters as utf-8
                p.setInput(getInputStream(url), "UTF_8");

                boolean inItem = false; //Keep track of if the parser is inside an item tag or not

                while(p.getEventType() != XmlPullParser.END_DOCUMENT){
                    //Loop until it is at the end of the document

                    if(p.getEventType() == XmlPullParser.START_TAG){
                        //If we are iterating over a START tag, then...

                        if(p.getName().equalsIgnoreCase("item")){
                            //We are now inside an item tag.
                            inItem = true;
                        } else if(p.getName().equalsIgnoreCase("title") && inItem) {
                            //Set temp title variable to the text read from this tag
                            title = p.nextText();
                        } else if(p.getName().equalsIgnoreCase("description") && inItem){
                            //In roadworks, description contains: Start date, end date, delay information. Seperated by <br />
                            //In incidents, description contains: general description of incident

                            String desc_tmp = p.nextText();
                            if(desc_tmp.contains("<br />")){
                                //if there is <br /> it means it is a roadwork with dates
                                String[] splitDescriptions = desc_tmp.split("<br />");
                                try {

                                    //Need to parse [0] into a date object
                                    SimpleDateFormat formatStart = new SimpleDateFormat("EE, dd MMMM yyyy - HH:mm", Locale.ENGLISH);
                                    splitDescriptions[0] = splitDescriptions[0].replace("Start Date: ", "");
                                    startDate = formatStart.parse(splitDescriptions[0]);

                                    //Need to parse [1] into a date object
                                    SimpleDateFormat formatEnd = new SimpleDateFormat("EE, dd MMMM yyyy - HH:mm", Locale.ENGLISH);
                                    splitDescriptions[1] = splitDescriptions[1].replace("End Date: ", "");
                                    endDate = formatEnd.parse(splitDescriptions[1]);

                                } catch(ParseException exception){
                                    Log.e("Error", exception.getMessage());
                                }

                                //Set the description if there is any left
                                if(splitDescriptions.length > 2){
                                    description = splitDescriptions[2];
                                } else {
                                    description = "No description found";
                                }
                            } else {
                                description = desc_tmp;
                            }

                        } else if(p.getName().equalsIgnoreCase("georss:point") && inItem){
                            //This is the format of "1.000 -1.000" where the first one is latitude and second is longitude
                            String[] lat_lng_tmp = p.nextText().split(" ");

                            latitude = Float.parseFloat(lat_lng_tmp[0]);

                            longitude = Float.parseFloat(lat_lng_tmp[1]);
                        } else if(p.getName().equalsIgnoreCase("pubDate") && inItem){
                            String date_tmp = p.nextText();

                            //Need to format this temp date variable into a date object
                            try {
                                //Need to parse date_tmp into a date object
                                SimpleDateFormat formatPubDate = new SimpleDateFormat("E, dd MMMM yyyy HH:mm:ss z", Locale.ENGLISH);
                                pubDate = formatPubDate.parse(date_tmp);

                            } catch(ParseException exception){
                                Log.e("Error", exception.getMessage());
                            }
                        }

                    } else if(p.getEventType() == XmlPullParser.END_TAG && p.getName().equalsIgnoreCase("item")){
                        //If this is an item closing tag set inside item to false
                        inItem = false;

                        //Create the FeedItem object from the temporary data
                        FeedItem rssObject = new FeedItem(title, description, latitude, longitude, pubDate, startDate, endDate, type);
                        //Add the new object to the array
                        items.add(rssObject);
                    }

                    p.next(); //go to the next one
                }

            } catch (MalformedURLException ex) {
                //Catch when url is wrong
                exception = ex;
            } catch (XmlPullParserException ex) {
                //Catch when xml has issue parsing
                exception = ex;
            } catch (IOException ex) {
                //Catch when reading issues etc
                exception = ex;
            }

            return exception;
        }

        @Override
        protected void onPostExecute(Exception e) {
            super.onPostExecute(e);

            parsingDone.rssParsingDone(items);

        }



    }

}

