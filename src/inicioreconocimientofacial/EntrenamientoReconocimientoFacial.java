/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inicioreconocimientofacial;

import java.io.File;
import java.util.ArrayList;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.face.EigenFaceRecognizer;

/**
 *
 * @author jdanielh
 */
public class EntrenamientoReconocimientoFacial {

    public EntrenamientoReconocimientoFacial(){
        entrenar();
    }
    
    private void entrenar(){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        
        String rutaBase = System.getProperty("user.dir") + "/Datos/";
        File directorioDatos = new File(rutaBase);
        String[] listaPersonas = directorioDatos.list();
        
        System.out.println("Personas en la base de datos: ");
        for(String e: listaPersonas)
            System.out.println(e);
        
        ArrayList<Mat> imagenes=new ArrayList<>();
	ArrayList<Integer> etiquetas=new ArrayList<>();
        int numEtiqueta = 0;
        
        for(String nombrePersona: listaPersonas){
            String rutaPersona = rutaBase + nombrePersona + "/";
            System.out.println("Leyendo imagenes...");
            
            File directorioImagenes = new File(rutaPersona);
            for(File nombreImagen: directorioImagenes.listFiles()){
                System.out.println(nombreImagen.getAbsolutePath());
                etiquetas.add(numEtiqueta);
                Mat imagenActual = Imgcodecs.imread(nombreImagen.getAbsolutePath(), 0);
                imagenes.add(imagenActual);
            }
            numEtiqueta++;
        }
        
        MatOfInt etiquetasMat = new MatOfInt();
        etiquetasMat.fromList(etiquetas);
        
        EigenFaceRecognizer efr=EigenFaceRecognizer.create();
	System.out.println("Empezando entrenamiento...");
	efr.train(imagenes, etiquetasMat);
        
        efr.write("modeloEigenFace.xml");
        System.out.println("El modelo ha sido almacenado");
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        new EntrenamientoReconocimientoFacial().entrenar();
    }
    
}
