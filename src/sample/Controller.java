package sample;
import Klasa.Narzedzie;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;

import java.awt.*;
import java.io.File;
import java.awt.image.BufferedImage;
//import java.awt.ArrayList;
import java.util.ArrayList;

import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;
import javafx.scene.input.MouseEvent;


public class Controller {
    @FXML
    private Canvas canvas, canvasP;

    @FXML
    private ColorPicker wybierzKolor = new ColorPicker();

    @FXML
    private ComboBox<String> wybierzNarzedzie;

    @FXML
    private CheckBox gumka, wypelnienieCB;

    @FXML
    private Spinner<Integer> rozmiarNarzedzia = new Spinner<Integer>();
    private double pierwszyX, pierwszyY, ostatniX, ostatniY;

    private Narzedzie.narzedzie wybierz;
    private GraphicsContext g;
    private GraphicsContext gP;

    private ArrayList <Image> doCofania = new ArrayList<Image>();
    private ArrayList <Image> doPonawiania = new ArrayList<Image>();

    public void initialize() {
        g = canvas.getGraphicsContext2D();
        gP = canvasP.getGraphicsContext2D();



        final int initialValue = 12;
        wybierzKolor.setValue(Color.BLACK);
        wybierzNarzedzie.getItems().addAll("Pędzel", "Linia", "Prostokąt","Owal");
        wybierzNarzedzie.getSelectionModel().selectFirst();

        wybierzNarzedzie.getSelectionModel().getSelectedItem();
        wybierzCB();

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 35, initialValue);
        rozmiarNarzedzia.setValueFactory(valueFactory);

    }

    public void onMousePressed(MouseEvent e) {
        this.pierwszyX = e.getX();
        this.pierwszyY = e.getY();

//        this.ostatniX = e.getX();
//        this.ostatniY = e.getY();

        switch (wybierz) {
            case PEDZEL:
                pedzel(e);
                break;
        }
    }

    public void onMouseRelease(MouseEvent e) {
        this.ostatniX = e.getX();
        this.ostatniY = e.getY();
        switch (wybierz) {
            case LINIA:
               linia(e);
                break;
            case PROSTOKAT:
                prostokat(e);
                break;
            case OWAL:
                owal(e);
                break;
        }
        Image snapshot = canvas.snapshot(null, null);
        doCofania.add(snapshot);
        doPonawiania.clear();
    }

    public void onMouseDragged(MouseEvent e) {
        this.ostatniX = e.getX();
        this.ostatniY = e.getY();

        switch (wybierz) {
            case PEDZEL:
                pedzel(e);
                break;
            case GUMKA:
                gumka(e);
                break;
            case LINIA:
                liniaAnimacja();
                break;
            case PROSTOKAT:
                prostokatAnimacja();
                break;
            case OWAL:
                owalAnimacja();
                break;
        }

    }

    public void pedzel(MouseEvent e) {
        double x = e.getX() - rozmiarNarzedzia.getValue() / 2;
        double y = e.getY() - rozmiarNarzedzia.getValue() / 2;

        g.setFill(wybierzKolor.getValue());
        g.fillRect(x, y, rozmiarNarzedzia.getValue(), rozmiarNarzedzia.getValue());


    }

    public void gumka(MouseEvent e) {
        double x = e.getX() - rozmiarNarzedzia.getValue() / 2;
        double y = e.getY() - rozmiarNarzedzia.getValue() / 2;

        g.clearRect(x, y, rozmiarNarzedzia.getValue(), rozmiarNarzedzia.getValue());
    }

    public void prostokat(MouseEvent e) {

        double szerokosc = Math.abs(ostatniX - pierwszyX);
        double wysokosc = Math.abs(ostatniY - pierwszyY);

        if(wypelnienieCB.isSelected())
        {
            g.setFill(wybierzKolor.getValue());
            g.fillRect(Math.min(pierwszyX, ostatniX), Math.min(pierwszyY,ostatniY), szerokosc, wysokosc);
        }else {
            g.setStroke(wybierzKolor.getValue());
            g.setLineWidth(rozmiarNarzedzia.getValue());
            g.strokeRect(Math.min(pierwszyX, ostatniX), Math.min(pierwszyY,ostatniY), szerokosc, wysokosc);
        }

    }
    public void owal(MouseEvent e) {

        double szerokosc = Math.abs(ostatniX - pierwszyX);
        double wysokosc = Math.abs(ostatniY - pierwszyY);


        if(wypelnienieCB.isSelected())
        {
            g.setFill(wybierzKolor.getValue());
            g.fillOval(Math.min(pierwszyX, ostatniX), Math.min(pierwszyY,ostatniY), szerokosc, wysokosc);
        }else {
            g.setStroke(wybierzKolor.getValue());
            g.setLineWidth(rozmiarNarzedzia.getValue());
            g.strokeOval(Math.min(pierwszyX, ostatniX), Math.min(pierwszyY,ostatniY), szerokosc, wysokosc);
        }


    }

    public void linia(MouseEvent e) {


        g.setLineWidth(rozmiarNarzedzia.getValue());
        g.setStroke(wybierzKolor.getValue());
        g.strokeLine(pierwszyX, pierwszyY, ostatniX, ostatniY);

    }

    public void liniaAnimacja()
    {
        gP.setLineWidth(rozmiarNarzedzia.getValue());
        gP.setStroke(wybierzKolor.getValue());
        gP.clearRect(0, 0, canvasP.getWidth() , canvasP.getHeight());
        gP.strokeLine(pierwszyX, pierwszyY, ostatniX, ostatniY);
    }
    public void prostokatAnimacja()
    {
        double szerokosc = Math.abs(ostatniX - pierwszyX);
        double wysokosc = Math.abs(ostatniY - pierwszyY);

        if(wypelnienieCB.isSelected())
        {
            gP.setFill(wybierzKolor.getValue());
            gP.clearRect(0, 0, canvasP.getWidth() , canvasP.getHeight());
            gP.fillRect(Math.min(pierwszyX, ostatniX), Math.min(pierwszyY,ostatniY), szerokosc, wysokosc);
        }
        else
        {
            gP.setStroke(wybierzKolor.getValue());
            gP.setLineWidth(rozmiarNarzedzia.getValue());
            gP.clearRect(0, 0, canvasP.getWidth() , canvasP.getHeight());
            gP.strokeRect(Math.min(pierwszyX, ostatniX), Math.min(pierwszyY,ostatniY), szerokosc, wysokosc);
        }
    }

    public void owalAnimacja()
    {
        double szerokosc = Math.abs(ostatniX - pierwszyX);
        double wysokosc = Math.abs(ostatniY - pierwszyY);
        if(wypelnienieCB.isSelected())
        {
            gP.setFill(wybierzKolor.getValue());
            gP.clearRect(0, 0, canvasP.getWidth() , canvasP.getHeight());
            gP.fillOval(Math.min(pierwszyX, ostatniX), Math.min(pierwszyY,ostatniY), szerokosc, wysokosc);
        }
        else
        {
            gP.setStroke(wybierzKolor.getValue());
            gP.setLineWidth(rozmiarNarzedzia.getValue());
            gP.clearRect(0, 0, canvasP.getWidth() , canvasP.getHeight());
            gP.strokeOval(Math.min(pierwszyX, ostatniX), Math.min(pierwszyY,ostatniY), szerokosc, wysokosc);
        }

    }

    public void wybierzCB() {

        if (gumka.isSelected()) {
            wybierz = Narzedzie.narzedzie.GUMKA;
        } else {
            if (wybierzNarzedzie.getSelectionModel().isSelected(0)) {
                wybierz = Narzedzie.narzedzie.PEDZEL;
            } else if (wybierzNarzedzie.getSelectionModel().isSelected(1)) {
                wybierz = Narzedzie.narzedzie.LINIA;
            } else if (wybierzNarzedzie.getSelectionModel().isSelected(2)) {
                wybierz = Narzedzie.narzedzie.PROSTOKAT;
            } else if (wybierzNarzedzie.getSelectionModel().isSelected(3)) {
                wybierz = Narzedzie.narzedzie.OWAL;
            }
        }

    }


    public void wyczysc() {
        g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gP.clearRect(0, 0, canvasP.getWidth(), canvasP.getHeight());
    }

    public void cofnij() {

        if(doCofania.size() > 1)
        {
            doPonawiania.add(doCofania.get(doCofania.size() - 1));
            doCofania.remove(doCofania.size() - 1);
            Image img = doCofania.get(doCofania.size() - 1) ;

            g.drawImage(img,0,0);
            gP.drawImage(img,0,0);
        }

    }

    public void ponow() {
        if(doPonawiania.size() > 0)
        {
            doCofania.add(doPonawiania.get(doPonawiania.size() - 1));
            doPonawiania.remove(doPonawiania.size() - 1);
            Image img = doCofania.get(doCofania.size() - 1) ;

            g.drawImage(img,0,0);
            gP.drawImage(img,0,0);
        }

    }
    public void zapisz() {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Zapisz obrazek");
        FileChooser.ExtensionFilter png = new FileChooser.ExtensionFilter("PNG (*.png)", "*.png");
        FileChooser.ExtensionFilter jpg = new FileChooser.ExtensionFilter("JPG (*.jpg)", "*.jpg");
        FileChooser.ExtensionFilter gif = new FileChooser.ExtensionFilter("GIF (*.gif)", "*.gif");

        fileChooser.getExtensionFilters().add(png);
        fileChooser.getExtensionFilters().add(jpg);
        fileChooser.getExtensionFilters().add(gif);
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try {
                Image snapshot = canvas.snapshot(null, null);
                ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", file);
            } catch (Exception e) {
                System.out.println("Blad zapisu! " + e);
            }
        }

    }

    public void otworz() {
        Stage stage = new Stage();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Otworz obrazek");
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter(".png, *.jpg, *.gif ", "*.png", "*.jpg", "*.gif"));
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try {
                //ImageIO.read(file);
                Image image = new Image(file.toURI().toString());
                g.drawImage(image, 0, 0);

            } catch (Exception e) {
                System.out.println("Blad otwierania pliku! " + e);
            }
        }
    }

    public void wyjdz() {
        Platform.exit();

    }
}
