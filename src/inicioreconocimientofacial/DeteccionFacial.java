/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inicioreconocimientofacial;

import com.demo.utils.Utils;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;

/**
 *
 * @author jdanielh
 */
public class DeteccionFacial {

    public static String rutaBase = System.getProperty("user.dir");
    public static String rutaClasificador = rutaBase + "/recursos/DeteccionFacial/haarcascade_frontalface_alt.xml";
    public static String imgEntradaNombre = rutaBase + "/recursos/DeteccionFacial/input3.jpg";
    public static String imgSalidaNombre = rutaBase + "/recursos/DeteccionFacial/output3.jpg";
    public static String rutaPersona = rutaBase;
    
    public static void main(String[] args) {
        String nombrePersona = "Daniel";
        crearCarpetaPersona(nombrePersona);
        try {
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            System.out.println("Libreria Cargada correctamente...");
            Mat imgMatriz = Imgcodecs.imread(imgEntradaNombre, 1);
            if (!imgMatriz.empty()) {
                // Deteccion Facial
                detectAndDisplay(imgMatriz);
                File outputfile = new File(imgSalidaNombre);
                ImageIO.write(Utils.matToBufferedImage(imgMatriz), "jpg", outputfile);
                System.out.println("Done!!");
            } else {
                System.out.println("No se encontro la imagen!");
            }
        } catch (IOException e) {
            System.out.println("Excepcion de I/O");
            e.printStackTrace();
        }
    }
    
    public static void crearCarpetaPersona(String nombrePersona){
        String rutaDatos = rutaBase + "/Datos";
        rutaPersona = rutaDatos + "/" + nombrePersona;
        
        File carpeta = new File(rutaPersona);
        if(!carpeta.exists()){
            System.out.println("Carpeta creada en: " + rutaPersona);
            carpeta.mkdirs();
        }
    }

    public static void detectAndDisplay(Mat imgMatriz) throws IOException {
        MatOfRect rostros = new MatOfRect();
        Mat imgMatrizGrises = new Mat();
        int tamañoAbsolutoRostro = 0;
        CascadeClassifier faceCascade = new CascadeClassifier();
        Mat imgMatrizAux = new Mat();
        imgMatriz.copyTo(imgMatrizAux);

        faceCascade.load(rutaClasificador);
        // Convertir imgMatriz en escala de grises
        Imgproc.cvtColor(imgMatriz, imgMatrizGrises, Imgproc.COLOR_BGR2GRAY);
        // Ecualizar el histograma de imgMatriz para mejorar el contraste
        Imgproc.equalizeHist(imgMatrizGrises, imgMatrizGrises);

        // Calcular el tamaño minimo del rostro(1% de imgMatriz altura, en nuestro caso)
        int altura = imgMatrizGrises.rows();
        if (Math.round(altura * 0.2f) > 0) {
            tamañoAbsolutoRostro = Math.round(altura * 0.01f);
        }

        // detectar rostros
        faceCascade.detectMultiScale(imgMatrizGrises, rostros, 1.1, 2, 0 | Objdetect.CASCADE_SCALE_IMAGE,
                new Size(tamañoAbsolutoRostro, tamañoAbsolutoRostro), new Size(altura, altura));

        // cada rectangulo en rostros es un rostro 
        Rect[] arregloRostrosRects = rostros.toArray();
        System.out.println("Numero de rostros detectados = " + arregloRostrosRects.length);
        int contador = 1;
        for (Rect rect : arregloRostrosRects) {
            Rect r = new Rect(new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height));
            Mat imgRecortada = new Mat(imgMatrizAux, r);
            Mat imgRedimensionada = new Mat();
            Size sz = new Size(100,100);
            Imgproc.resize(imgRecortada, imgRedimensionada, sz );
            String imgSalidaRostro = rutaPersona + "/Rostro"+(contador++)+".jpg";
            File outputfile = new File(imgSalidaRostro);
            ImageIO.write(Utils.matToBufferedImage(imgRedimensionada), "jpg", outputfile);
        }

    }

}
