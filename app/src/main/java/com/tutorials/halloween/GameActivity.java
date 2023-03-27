package com.tutorials.halloween;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class GameActivity extends AppCompatActivity implements
        View.OnTouchListener, GestureDetector.OnGestureListener {

    private TextView secondsRemained;
    private TextView haslo;
    private LinearLayout yellow;
    private int screenX;
    private CountDownTimer timer;
    private String[] puzzlesArray;
    private short playerPoints = 0;
    private GestureDetector gestureDetector;
    private ImageView btnNo;
    private ImageView btnYes;

    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);



        gestureDetector = new GestureDetector(this, this);

        secondsRemained = findViewById(R.id.time);
        haslo = findViewById(R.id.textView);
        yellow = findViewById(R.id.buttons);

        yellow.setOnTouchListener(this);
        secondsRemained.setOnTouchListener(this);

        btnNo = findViewById(R.id.no);
        btnYes = findViewById(R.id.yes);

        timer =  new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {
                secondsRemained.setText("Przyłóż telefon do czoła!");
                haslo.setText("Gra rozpocznie się za: " + millisUntilFinished / 1000);
            }

            public void onFinish() {

                btnNo.setVisibility(View.VISIBLE);
                btnYes.setVisibility(View.VISIBLE);

                secondsRemained.setText("Już!");

                play();

                timer =  new CountDownTimer(300000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        secondsRemained.setText("Tyle pozostało Ci sekund do końca rozgrywki: " + millisUntilFinished / 1000);
                    }

                    public void onFinish() {
                        secondsRemained.setText("Czas się skończył!");
                        stopPlayer();

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GameActivity.this); //there was a problem with this line
                        alertDialogBuilder.setMessage("GameOver!\nZdobyte punkty: " + playerPoints)
                                .setCancelable(false)
                                .setPositiveButton("Zagraj jeszcze raz", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }).setNegativeButton("Wyjdź do menu głównego", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }
                }.start();

                // Get a Display object to access screen details
                Display display = getWindowManager().getDefaultDisplay();
                // Load the resolution into a Point object
                Point size = new Point();
                display.getSize(size);

                screenX = size.x;

                loadPuzzlesArray();
                haslo.setText(randomPuzzleToGuess());
            }
        }.start();


    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(v.getId() == R.id.buttons) {
            gestureDetector.onTouchEvent(event);
            return true;
        }
        else if(v.getId() == R.id.time) {
            haslo.setText(randomPuzzleToGuess());
        }return false;
    }

    private void playerHasGivenUp(){
        playerPoints--;
        haslo.setText(randomPuzzleToGuess());
    }

    private void playerHasGuessed(){
        playerPoints++;
        haslo.setText(randomPuzzleToGuess());
    }

    private void loadPuzzlesArray(){
        puzzlesArray = new String[]{
                "Wigilia Wszystkich Świętych", "szczurze nory", "nocne stworzenia", "zasnuty pajęczynami", "nawiedzony strych", "uprawiać czary", "spiczaste uszy", "przekrwione ślepia", "płaski nos",
                "oślinione wargi", "wycie do księżyca", "zły goblin", "nawiedzony las", "spoczywaj w spokoju", "kołek na wampiry", "zamek Draculi", "wampirze kły", "kościotrup", "ofiara z dziewicy",
                "nie z tego świata", "nadchodzące szaleństwo", "portal", "otwarty grób", "czosnek i srebro", "strachy na lachy", "strzykawki do zabawki", "karuzela śmierci", "chichot wiedźmy",
                "świst wiatru", "ogień oczyszczenia", "zły los", "pomiędzy niebem a ziemią", "piekielny ogień", "wieczna tułaczka", "podzwaniające łańcuchy", "skrzeczące wrony", "złośliwe chochliki",
                "złoty włos", "garniec złota", "wróżka zębuszka", "dzień wszystkich zmarłych", "sexi przebranie", "torba na cukierki", "potwory i upiory", "powstali z grobów", "dusze potępione",
                "strach przed śmiercią", "scarry terry", "Czarny kot", "Nieprzechodzenie pod drabiną", "Trick-or-treating", "cukierek albo psikus", "wysuszone kości", "wywar z trupa", "trupi jad",
                "dziecko w kotle", "napar z nietoperza", "pochówek wiedźmy", "zła wiedźma", "domek z piernika", "psotna miotła", "latanie na miotle", "gnijące zwłoki", "topniejący wosk", "peleryna Draculi",
                "kocioł czarownika", "odlotowe przebranie", "straszny kostium", "krypta przycmentarna", "gałki oczne", "pazury i kły", "pełnia księżyca", "pole dyniowe", "nawiedzony cmentarz",
                "wydrążona dynia", "lampa z dyni", "miasteczko halloween", "upiorne szpony", "koszmarne sny", "koszmar na jawie", "światło księżyca", "Frankenstein", "eliksir życia", "życie pozagrobowe",
                "nieumarli", "kaplica czaszek", "magiczna różdżka", "czarnoksięstwo", "ciemna strona mocy", "bezbożna ziemia", "polowanie na czarownice", "prank", "nieokreślony byt", "krew dziecka",
                "Brama do innego świata", "Rytuał przywołania", "zgniłe mięso", "królestwo duchów", "Nocne Zmory", "figury woskowe", "ofiara całopalna", "okultyzm", "złowieszczy szept", "sekretny kult",
                "przerażające opowieści", "ludzkie ofiary", "straszne istoty", "wpędzony do grobu", "zły dotyk", "zmrozić krew w żyłach", "nawiedzony dom", "przeszywający dreszcz", "dziwne mroczne zaułki",
                "schorzała wyobraźnia", "ekstrawagancka wyobraźnia", "strach i niepokój", "chrobotanie szczurów", "przeżarte ściany", "diabelskie pandemonium", "niewyjaśnione dźwięki", "bezsenne noce",
                "kryjówka czarownic", "bluźniercze plotki", "fantastyczne legendy", "pradawna magia", "mroczna aura", "odczytać formułę", "zimny dreszcz", "cela wiedźmy", "ślady ludzkich zębów",
                "dziecięce krzyki", "najciemniejsza noc","uprawiać czary", "płynące widmo", "zapisy inkantacji", "wiele dziwnych rzeczy", "zakazana historia", "kręte i wąskie uliczki", "nocne stworzenia",
                "zasnuty pajęczynami", "nawiedzony strych", "hipnotyczny wpływ", "poza światem żyjących", "szuranie szczurów", "skrobanie szczurów", "odrażający chichot", "ohydne okropniści",
                "niepokojące odgłosy", "przeraźliwe ryki", "gęsia skórka", "mroczny labirynt", "chaotyczne sny", "zły wpływ", "stary dom", "mroczne zainteresowania", "mroczne fantazje", "upiorne wizje",
                "potężna istota", "czarna magia", "mutacje", "urojenia słuchowe", "mroczne czeluści", "szaleńcze historie", "historie o duchach", "srebrny krucyfiks", "przybycie demonów", "otchłanie piekieł",
                "niewolnicy Szatana", "plugawe czyny", "bluźniercze obrzędy", "złowroga bliskość", "ryki i wrzaski", "złe przeczucia", "podpisać krwią", "obłędne melodie", "nieziemska poświata",
                "żółtobiałe kły", "osobliwa mgła", "zły czar", "szponiaste palce", "straszny pająk", "skąpy kostium", "kombinezon z lajkry", "skompy kostium", "kostium batmana", "kostium kobiety kot",
                "naszyjnik z czosnku", "sposoby na wampiry", "wbić kołek w serce", "wyć do księżyca", "pisk nietoperzy", "skąpy kostium", "kostium dzwoneczka", "strach na wróble", "bezgłowy jeździec",
                "wyszczerbione zęby", "trupie czaszki", "czarny kot", "Bu!", "powolny zombie", "sztuczna rana", "zaświaty", "palenie ognisk", "granica", "wyrazy czci", "odstraszanie złych duchów", "czyściec",
                "kult świętych", "kolorowe stroje", "zapalić świece", "płomień świecy", "Mumia", "Nosferatu", "pukanie do drzwi", "pojemnik na cukierki", "kontrowersje", "pogańska geneza", "zabawa dla dzieci",
                "odskocznia od rzeczywistosci", "straszny labirynt", "Duszek Kacperek", "kolor pomarańczowy", "cukierek lub psikus", "super zabawa", "wampiry i wilkołaki", "przebierańcy", "wyssać krew",
                "udekorować dom", "lampion z dyni", "oglądanie horrorów", "straszne historie", "maskarada", "bluźniercze obrzędy", "Duchy i szkielety", "pogańska tradycja", "kapelusz wiedźmy", "Apple Bobbing"};
    }

    private String randomPuzzleToGuess(){

        Random generator = new Random();
        short randomNumber = (short) generator.nextInt(puzzlesArray.length - 1);
        return puzzlesArray[randomNumber];
    }

    @Override
    public boolean onDown(MotionEvent event) {
        if (event.getX() > screenX / 2){
            //zgadłem
            playerHasGuessed();
        }
        if (event.getX() < screenX / 2){
            //pasuję
            playerHasGivenUp();
        }
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    private void play() {
        if(mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.gamesong);
        }
        mediaPlayer.start();
    }

    private void stopPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopPlayer();
    }
}
