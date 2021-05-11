/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inicioreconocimientofacial;

import java.io.File;
import org.opencv.core.Core;
import org.opencv.face.EigenFaceRecognizer;

/**
 *
 * @author jdanielh
 */
public class ReconocimientoFacialPruebas {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        
        String rutaBase = System.getProperty("user.dir") + "/Datos/";
        File directorioDatos = new File(rutaBase);
        String[] listaPersonas = directorioDatos.list();
        
        EigenFaceRecognizer efr = EigenFaceRecognizer.create();
        efr.read("modeloEigenFace.xml");
        
    }
    
}
