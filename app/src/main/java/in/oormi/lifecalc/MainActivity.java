package in.oormi.lifecalc;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    SeekBar sb[] = new SeekBar[8];
    final TextView tv[] = new TextView[12];
    int vals[] = {8, 4, 2, 1, 2, 1}; //defaults
    int sbids[] = {R.id.seekBar1, R.id.seekBar2, R.id.seekBar3, R.id.seekBar4, R.id.seekBar5,
            R.id.seekBar6, R.id.seekBar7, R.id.seekBar8};
    final int tvids[] = {R.id.textTime1, R.id.textTime2, R.id.textTime3, R.id.textTime4,
            R.id.textTime5, R.id.textTime6, R.id.textTime7, R.id.textTime8, R.id.textTime9,
            R.id.textTime10, R.id.textAge, R.id.textDaysLeft};
    public int age = 50, remage = 40, maxage = 90; //defaults

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main);

        for(int i=0; i < 8; i++) {
            sb[i] = (SeekBar) findViewById(sbids[i]);
            tv[i] = (TextView) findViewById(tvids[i]);
            sb[i].getProgressDrawable().setColorFilter(Color.rgb(120,0,0), PorterDuff.Mode.SCREEN);

            final int j = i;
            sb[i].setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    tv[j].setText(String.format("%s%s", String.valueOf(progress),
                            getString(R.string.hours)));
                    doCalc(j, progress);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

        }

        for(int i=8; i < 12; i++) {
            tv[i] = (TextView) findViewById(tvids[i]);
        }

        sb[6].setProgress(age); //inits calc
        sb[6].getProgressDrawable().setColorFilter(Color.rgb(0,60,127),
                PorterDuff.Mode.SCREEN);
        sb[7].setEnabled(false);

    }
    public void doCalc(int bar, int value){
        if(bar<vals.length) vals[bar] = value;
        int totalhrs = 0, remhrs = 24;
        for(int v=0;v<vals.length;v++) totalhrs +=vals[v];
        remhrs = 24 - totalhrs;
        remhrs = (remhrs<0)?0:remhrs;
        tv[7].setText(String.format("%s%s", String.valueOf(remhrs), getString(R.string.hours)));

        if(bar==6){
            age = value;
            remage = maxage - age;
            tv[6].setText(String.format("%s%s", getString(R.string.remagetxt),
                    String.valueOf(remage)));
            tv[10].setText(String.format("%s%s", getString(R.string.curagetxt),
                    String.valueOf(age)));
        }

        int tremlife, twholelife;
        tremlife = (int) ((float)remage*((float)remhrs/24.0f));
        tv[8].setText(String.format("%s%s", String.valueOf(tremlife), getString(R.string.years)));

        twholelife = (int) ((float)maxage*((float)remhrs/24.0f));
        tv[9].setText(String.format("%s" + getString(R.string.years), String.valueOf(twholelife)));
        sb[7].setProgress(twholelife);

        float cb = 255.0f*(float)remage/(float)maxage;
        float cg = 255.0f*(float)twholelife/(float)maxage;
        float cr = 255.0f - cg/2.0f - cb/2.0f;
        sb[7].getProgressDrawable().setColorFilter(Color.rgb((int)cr, (int)cg, (int)cb),
                PorterDuff.Mode.MULTIPLY);

        int days = (int)((float)(remage*365.0f*remhrs)/24.0f);
        tv[11].setText(String.format("%s%s", String.valueOf(days), getString(R.string.daysleft)));
    }
    private ShareActionProvider mShareActionProvider;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.infomenu, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_item_share);

        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider)  MenuItemCompat.getActionProvider(item);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                "https://play.google.com/store/apps/details?id=in.oormi.lifecalc");
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Check out this app!");
        setShareIntent(shareIntent);
        return true;
    }

    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.info:
                //Toast.makeText(this, "Menu Item 1 selected", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(this, InfoActivity.class);
                startActivity(i);
                break;
        }
        return true;
    }
}
