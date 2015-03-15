package net.hackcasual.freeciv.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.*;
import android.widget.*;
import net.hackcasual.freeciv.*;
//import net.hackcasual.freeciv.views.FreeCiv;
import net.hackcasual.freeciv.views.LoadGame;
import net.hackcasual.freeciv.views.NativeAwareFragment;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

public class MapViewFragment extends NativeAwareFragment {

    NativeHarness nh;
    List<NativeHarness.AvailableCommand> currentOptions;
    DialogManager dm;
    private Bitmap mapView;
    boolean unitMenu = false;
    long tsBuf[] = new long[10];
    int tsi = -1, tsc = 0;
    AlertDialog overviewDialog;
    boolean isPaused;

    final BlockingQueue<MotionEvent> touchQueue = new LinkedBlockingQueue<MotionEvent>();
    private float oldZoomLong = 0;
    private int screenWidth;
    private int screenHeight;
    private int activityWidth;
    private int activityHeight;
    private int screenWidthOffset;
    private int screenHeightOffset;
    private LinearLayout unitCommandsView;
    private int iconSize;
    private Button endTurn;


    public MapViewFragment(){}


	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        final View rootView = inflater.inflate(R.layout.mapview, container, false);
        unitCommandsView = (LinearLayout) rootView.findViewById(R.id.unitCommands);
        Log.d("Freeciv.java", "start mapview fragment");
        nh = ((Civ)(this.getActivity().getApplication())).getNativeHarness();
        nh.getDialogManager().bindActivity(this.getActivity());
        nh.setMainFragment(this);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        activityWidth = display.getWidth();
        activityHeight = display.getHeight();
        iconSize = activityHeight /10;

        endTurn = (Button) rootView.findViewById(R.id.end_turn);
        endTurn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nh.sendCommand(100);
            }
        });

        rootView.setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event){
                Log.d("MapViewFragment", "touched");
                setTouchQueue(event);
                return true;
            }
        });

        //((ImageView)getActivity().findViewById(R.id.map_view)).setImageBitmap(mapView); view not init at this time

        //Intent startServer = new Intent(this, CivService.class);

        //ComponentName cn = startService(startServer);
        //Log.i("FreeCiv", String.format("Done starting server %s", cn.toString()));
        //nh.runClient();

        final MapViewFragment me = this;

        (new Thread() {
            public void run() {
                MotionEvent toProcess = null;
                while (true) {
                    try {
                        if (touchQueue.isEmpty()) {
                            toProcess = touchQueue.take();
                        } else {
                            while (!touchQueue.isEmpty()) {
                                toProcess = touchQueue.take();
                            }
                        }

                    } catch (InterruptedException e) {
                        //Nothing
                    }
                    int unitCount = NativeHarness.touchEvent(((int)toProcess.getX() - screenWidthOffset)/2, ((int)toProcess.getY() - screenHeightOffset)/2, toProcess.getAction());
                    if (unitCount > 0) {
                        if (unitCount == 1) {
                            me.getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    me.showUnitMenu();
                                }
                            });
                        } else {
                            final int x = (int)toProcess.getX();
                            final int y = (int)toProcess.getY();
                            me.getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    me.showUnitSelection(x, y);
                                }
                            });
                        }
                    }

                }
            }
        }).start();
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("MapViewFragment","onActivityCreated");
        final ImageView image = (ImageView) getActivity().findViewById(R.id.map_view);
        ViewTreeObserver vto = image.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                screenHeight = image.getMeasuredHeight();
                screenWidth = image.getMeasuredWidth();
                screenWidthOffset = activityWidth - screenWidth;
                screenHeightOffset = activityHeight - screenHeight;
                NativeHarness.init(screenWidth/2, screenHeight/2);
                mapView = Bitmap.createBitmap(screenWidth/2, screenHeight/2, Bitmap.Config.RGB_565);
                image.getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        this.isPaused = false;
        //nh.reloadMap();
        //nh.updateDisplay();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.isPaused = true;
    }

    public boolean setTouchQueue(MotionEvent event) {
        Log.d("MapViewFragment", "new touch event : "+event.toString());
        touchQueue.add(event);
        return false;
    }

    public void showUnitMenu() {
        Log.d("MapViewFragment", "try to open unit menu");
        unitMenu = true;
        unitCommandsView.removeAllViews();
        List<NativeHarness.AvailableCommand> unitCommandes = nh.getAvailableCommandsForUnit();
        for(final NativeHarness.AvailableCommand command : unitCommandes) {
            ImageView im = new ImageView(getActivity().getApplicationContext());
            Drawable res = getResources().getDrawable(getResources().getIdentifier("drawable/order_"+command.toString().toLowerCase(), null,
                    getActivity().getApplicationContext().getPackageName()));
            im.setImageDrawable(res);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(iconSize, iconSize);
            layoutParams.setMargins(5, 5, 5, 5);
            im.setLayoutParams(layoutParams);
            im.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nh.sendCommand(command.ordinal());
                }
            });
            unitCommandsView.addView(im);

        }
        unitMenu = false;
    }

    public void showUnitSelection(int x, int y) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());

        builder.setTitle("Choose Unit");
        ListView units = new ListView(this.getActivity());
        units.setFocusable(true);
        final CityPresentUnitAdapter adapter = new CityPresentUnitAdapter(this.getActivity());
        units.setAdapter(adapter);
        //builder.setView(units);

        for (int i: nh.getUnitsOnTile(x, y)) {
            adapter.add(nh.getUnitById(i));
        }

        builder.setView(units);
        final AlertDialog shown = builder.create();
        units.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
                                    long arg3) {
                nh.focusOnUnit(adapter.getItem(pos).getUnitId());
                shown.dismiss();
                unitMenu = true;
                //TODO replace by unit action menu
                unitMenu = false;
            }

        });



        shown.show();

    }


    public void showDialog() {
        final CharSequence[] items = {"Alpha", "Bravo", "Charlie", "Delta", "Echo", "Foxtrot", "Golf", "Hotel", "India", "Kilo", "Lima", "Mike", "November"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle("Pick a color");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                Toast.makeText(getActivity().getApplicationContext(), items[item], Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void updateMapview(final ByteBuffer image, final Semaphore renderLock) {

        Log.i("FreeCiv", String.format("Updating mapview on thread: %d", Thread.currentThread().getId()));

        this.getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Log.i("FreeCiv", String.format("In UI thread %d", Thread.currentThread().getId()));

                image.rewind();
                mapView.copyPixelsFromBuffer(image);
                ((ImageView)getActivity().findViewById(R.id.map_view)).setImageBitmap(mapView);
                //renderLock.release();
                Log.i("FreeCiv", "gui thread released lock");
            }

        });
    }

    void runExercise(final Activity that) {
        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Log.i("FreeCiv", "Running exercise1");

                long curTime = System.currentTimeMillis();

                NativeHarness.exercise1();

                final long t1 = System.currentTimeMillis() - curTime;
                that.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((TextView)getActivity().findViewById(R.id.msg_bar)).setText(String.format("Exercise 1[1] took: %d : ms", t1));
                    }

                });


                curTime = System.currentTimeMillis();

                NativeHarness.exercise1();
                final long t2 = System.currentTimeMillis() - curTime;
                that.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((TextView)getActivity().findViewById(R.id.msg_bar)).setText(String.format("Exercise 1[2] took: %d : ms", t2));
                    }

                });



                curTime = System.currentTimeMillis();

                NativeHarness.exercise1();
                final long t3 = System.currentTimeMillis() - curTime;
                that.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((TextView)getActivity().findViewById(R.id.msg_bar)).setText(String.format("Exercise 1[3] took: %d : ms", t3));
                    }

                });



            }

        };

        t.start();
    }

    public void writeText(final String s, final Paint p, final int x, final int y) {
        this.getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Canvas cvs = new Canvas(mapView);
                cvs.drawText(s, x, y, p);
                ((ImageView)getActivity().findViewById(R.id.map_view)).setImageBitmap(mapView);
            }

        });
    }

/**    public boolean onPrepareOptionsMenu (Menu menu) {
        menu.clear();
        currentOptions = nh.getAvailableCommandsForUnit();
        Log.d("FreeCiv.java", "get unit option : "+currentOptions);
        if (unitMenu) {

            int count = 0;
            SubMenu more = null;
            for (NativeHarness.AvailableCommand command: currentOptions) {
                Log.d("FreeCiv", String.valueOf(command));

                //menu.add(command.name());

                if (++count > 5 && currentOptions.size() > 6) {
                    if (6 == count)
                        more = menu.addSubMenu("More");

                    more.add(0, command.ordinal(), 0, command.name());
                } else {
                    menu.add(0, command.ordinal(), 0, command.name());
                }

            }
        } else {
            menu.clear();

            menu.add(0, 100, 0, "End Turn");

            if (currentOptions.size() > 0) {
                SubMenu unitOrders = menu.addSubMenu(0, 101, 0, Civ.getUnitTypeById(nh.getFocusedUnitType()).getName());
                unitOrders.setIcon(new BitmapDrawable(Civ.getUnitTypeById(nh.getFocusedUnitType()).getIcon()));

                for (NativeHarness.AvailableCommand command: currentOptions) {
                    Log.d("FreeCiv", String.valueOf(command));
                    unitOrders.add(0, command.ordinal(), 0, command.name());
                }
            }

            menu.add(0, 102, 0, "Adjust Research");
            menu.add(0, 103, 0, "Status");
            menu.add(0, 104, 0, "Save");
            //TODO: Only cancel if we are currently going to perform an action
            //TODO: or maybe cancel anytime we hit menu
            menu.add(0, 105, 0, "Cancel");
        }
        return true;
    } */

    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case 101: showUnitMenu(); break;
            //case 102: showResearchActivity(); break;
            //case 103: showPlayerInfo(); break;
            case 104: NativeHarness.save(); break;
            default: nh.sendCommand(item.getItemId()); break;
        }



        return true;
    }

/*	public void showResearchActivity() {
        Log.d("FreeCiv.java", "showResearchActivity called");
		Intent researchViewer = new Intent(this, ResearchView.class);

		startActivity(researchViewer);
	}*/

    /*void showPlayerInfo() {
        Intent playerViewer = new Intent(this, PlayerView.class);

        startActivity(playerViewer);
    }*/

    @Override
    public void receiveTilesetUpdate(String info) {
        // implement interface requirement

    }

    @Override
    public void setConnectionStatus(boolean status) {
        if (status && !isConnected) {
            ((Civ)this.getActivity().getApplicationContext()).loadUniversals();
            isConnected = true;
            NativeHarness.tellServer("/set dispersion=5");

            if (this.getActivity().getIntent().hasExtra(LoadGame.SAVED_GAME_TAG)) {
                String savedGame = this.getActivity().getIntent().getStringExtra(LoadGame.SAVED_GAME_TAG);
                NativeHarness.tellServer(String.format("/load /sdcard/FreeCiv/%s", savedGame));
            }

            NativeHarness.tellServer("/start");
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d("MapViewFragment", "onConfigurationChanged");
        if (overviewDialog != null && overviewDialog.isShowing())
            overviewDialog.cancel();

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();

        NativeHarness.init(width, height);

        mapView = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        ((ImageView)getActivity().findViewById(R.id.map_view)).setImageBitmap(mapView);
    }

    //@Override
    public boolean onSearchRequested() {
        //((ImageView)findViewById(R.id.map_view)).setImageBitmap(nh.getOverview());

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());// overviewDialog = new AlertDialog(this);

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();

        float imageScale = 0.0f;
        int image_width, image_height;

        Bitmap theOverview = nh.getOverview();

        if (width < height) {
            float destWidth = width * 0.66f;
            imageScale = destWidth / theOverview.getWidth();
        } else {
            float destHeight = height * 0.55f;
            imageScale = destHeight / theOverview.getHeight();
        }

        if (imageScale > 2.5f)
            imageScale = 2.5f;

        image_width = (int) (theOverview.getWidth() * imageScale);
        image_height = (int) (theOverview.getHeight() * imageScale);

        final ImageView overviewView = new ImageView(this.getActivity());
        overviewView.setScaleType(ImageView.ScaleType.FIT_XY);
        overviewView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));

        final LinearLayout overviewHolder = new LinearLayout(this.getActivity());
        overviewHolder.setLayoutParams(new ViewGroup.LayoutParams(image_width, image_height));
        overviewHolder.addView(overviewView, image_width, image_height);

        Log.i("FreeCiv", String.format("From: %d, %d To: %d, %d", theOverview.getWidth(), theOverview.getHeight(), image_width, image_height));

        final BitmapDrawable filtered =new BitmapDrawable(theOverview);
        filtered.setAntiAlias(false);
        filtered.setFilterBitmap(false);


        final float scaleFactor = imageScale;



        //overviewView.setTouchDelegate(new TouchDelegate(null, overviewView) {});
        overviewView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                Log.i("FreeCiv", String.format("Touch: %d, %d", (int)(arg1.getX()), (int)(arg1.getY())));
                nh.positionFromOverview((int)(arg1.getX() / scaleFactor), (int)(arg1.getY() / scaleFactor));
                BitmapDrawable filtered = new BitmapDrawable(nh.getOverview());
                filtered.setAntiAlias(false);
                filtered.setFilterBitmap(false);
                overviewView.setImageDrawable(filtered);
                return false;
            }

        });
        overviewView.setImageDrawable(filtered);


        //builder.setView();
        //builder.

        overviewDialog = builder.create();


        overviewDialog.setView(overviewHolder);
        overviewDialog.setCanceledOnTouchOutside(true);
        overviewDialog.show();



        return false;  // don't go ahead and show the search box
    }

    public void doZoom(float zoomStartX, float zoomStartY, float x, float y) {
        float zoomLong = getZoomWidth(zoomStartX, zoomStartY, x, y);
        Log.d("MapViewFragment", "doZoom");
        float ratio = oldZoomLong /  zoomLong;
        Log.d("MapViewFragment","oldZoomLong : "+oldZoomLong+" zoomLong : "+zoomLong );

        if(zoomLong < oldZoomLong){
            Log.d("MapViewFragment", "screenWidth : "+screenWidth+"screenHeight : "+screenHeight+"ratio : "+ratio);
            int initx = Math.round(screenWidth * ratio);
            int inity = Math.round(screenHeight * ratio);
            NativeHarness.init(initx, inity);

        }
        if(zoomLong > oldZoomLong){
            Log.d("MapViewFragment", "screenWidth : "+screenWidth+"screenHeight : "+screenHeight+"ratio : "+ratio);
            int initx = Math.round(screenWidth * ratio);
            int inity = Math.round(screenHeight * ratio);
            NativeHarness.init(initx, inity);

        }

    }

    private float getZoomWidth(float zoomStartX, float zoomStartY, float x, float y) {
        float zoomx = zoomStartX - x;
        float zoomy = zoomStartY - y;
        return FloatMath.sqrt(zoomx * zoomx + zoomy * zoomy);
    }

    public void setZoomLong(float zoomStartX, float zoomStartY, float x, float y) {
        Log.d("MapViewFragment", "setZoom");
        oldZoomLong = getZoomWidth(zoomStartX, zoomStartY, x, y);
    }

}
