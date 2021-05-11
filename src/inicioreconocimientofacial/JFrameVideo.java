/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inicioreconocimientofacial;

import com.demo.utils.Utils;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.opencv.videoio.VideoCapture;

/**
 *
 * @author jdanielh
 */
public class JFrameVideo extends javax.swing.JFrame {

    String rutaBase = System.getProperty("user.dir");
    String rutaClasificador = rutaBase + "/recursos/DeteccionFacial/haarcascade_frontalface_alt.xml";
    String rutaPersona = rutaBase;
    String nombrePersona;

    private VideoThread videoThread = null;
    VideoCapture camara = null;
    Mat imgMatriz = new Mat();
    CascadeClassifier detectorRostros = new CascadeClassifier(rutaClasificador);
    MatOfRect rostrosDetectados = new MatOfRect();
    MatOfByte mem = new MatOfByte();

    void crearCarpetaPersona() {
        String rutaDatos = rutaBase + "/Datos";
        rutaPersona = rutaDatos + "/" + nombrePersona;

        File carpeta = new File(rutaPersona);
        if (!carpeta.exists()) {
            System.out.println("Carpeta creada en: " + rutaPersona);
            carpeta.mkdirs();
        }
    }

    class VideoThread implements Runnable {

        protected volatile boolean runnable = false;
        protected volatile int contador = 1;

        @Override
        public void run() {
            synchronized (this) {
                while (runnable) {
                    if (camara.grab()) {
                        try {
                            camara.retrieve(imgMatriz);
                            Graphics g = jPanel1.getGraphics();
                            
                            Mat imgMatrizGrises = new Mat();
                            int tamañoAbsolutoRostro = 0;

                            // Convertir imgMatriz en escala de grises
                            Imgproc.cvtColor(imgMatriz, imgMatrizGrises, Imgproc.COLOR_BGR2GRAY);
                            // Ecualizar el histograma de imgMatriz para mejorar el contraste
                            Imgproc.equalizeHist(imgMatrizGrises, imgMatrizGrises);

                            // Calcular el tamaño minimo del rostro(1% de imgMatriz altura, en nuestro caso)
                            int altura = imgMatrizGrises.rows();
                            if (Math.round(altura * 0.2f) > 0) {
                                tamañoAbsolutoRostro = Math.round(altura * 0.3f);
                            }

                            // detectar rostros
                            detectorRostros.detectMultiScale(imgMatrizGrises, rostrosDetectados, 1.1, 2, 0 | Objdetect.CASCADE_SCALE_IMAGE,
                                    new Size(tamañoAbsolutoRostro, tamañoAbsolutoRostro), new Size(altura, altura));

                            for (Rect rect : rostrosDetectados.toArray()) {
                                //Dibujar rectangulo verde
                                Imgproc.rectangle(imgMatriz, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                                        new Scalar(0, 255, 0));

                                //Recortar fotografia del rostro y guardarlo en su carpeta
                                Mat imgRecortada = new Mat(imgMatrizGrises, rect);
                                Mat imgRedimensionada = new Mat();
                                Size sz = new Size(200, 200);
                                Imgproc.resize(imgRecortada, imgRedimensionada, sz);
                                String imgSalidaRostro = rutaPersona + "/Rostro" + (contador++) + ".jpg";
                                File outputfile = new File(imgSalidaRostro);
                                ImageIO.write(Utils.matToBufferedImage(imgRedimensionada), "jpg", outputfile);
                            }
                            //Mostrar las imagenes en el jPanel
                            Imgcodecs.imencode(".bmp", imgMatriz, mem);
                            Image im = ImageIO.read(new ByteArrayInputStream(mem.toArray()));
                            BufferedImage buff = (BufferedImage) im;
                            if (g.drawImage(buff, 0, 0, getWidth(), getHeight(), 0, 0, buff.getWidth(), buff.getHeight(), null)) {
                                if (runnable == false) {
                                    System.out.println("En pausa...");
                                    this.wait();
                                }
                            }

                            //A las 200 fotografias se detiene el video
                            if (contador >= 200) {
                                jButtonDetener.doClick();
                            }

                        } catch (IOException ex) {
                            Logger.getLogger(JFrameVideo.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(JFrameVideo.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        }

    }

    /**
     * Creates new form JFrameVideo
     */
    public JFrameVideo() {
        initComponents();
        jButtonIniciar.setEnabled(false);
        jButtonDetener.setEnabled(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jButtonIniciar = new javax.swing.JButton();
        jButtonDetener = new javax.swing.JButton();
        jButtonSalir = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jTextNombre = new javax.swing.JTextField();
        jButtonGuardar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );

        jButtonIniciar.setFont(new java.awt.Font("Open Sans", 0, 16)); // NOI18N
        jButtonIniciar.setText("Iniciar");
        jButtonIniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonIniciarActionPerformed(evt);
            }
        });

        jButtonDetener.setFont(new java.awt.Font("Open Sans", 0, 16)); // NOI18N
        jButtonDetener.setText("Detener");
        jButtonDetener.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDetenerActionPerformed(evt);
            }
        });

        jButtonSalir.setFont(new java.awt.Font("Open Sans", 0, 16)); // NOI18N
        jButtonSalir.setText("Salir");
        jButtonSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSalirActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel1.setText("Nombre: ");

        jTextNombre.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jTextNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextNombreActionPerformed(evt);
            }
        });

        jButtonGuardar.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jButtonGuardar.setText("Guardar");
        jButtonGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGuardarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(118, 118, 118)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jButtonIniciar, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonDetener, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonSalir, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextNombre)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(123, 123, 123))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(62, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonGuardar))
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonIniciar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonDetener, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonIniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonIniciarActionPerformed
        // TODO add your handling code here:
        camara = new VideoCapture(0); // capturar video de la camara por default
        videoThread = new VideoThread(); //create object of threat class
        Thread t = new Thread(videoThread);
        t.setDaemon(true);
        videoThread.runnable = true;
        t.start();
        jButtonIniciar.setEnabled(false);  // deactivate start button
        jButtonDetener.setEnabled(true);  //  activate stop button
    }//GEN-LAST:event_jButtonIniciarActionPerformed

    private void jButtonDetenerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDetenerActionPerformed
        // TODO add your handling code here:
        videoThread.runnable = false;
        jButtonDetener.setEnabled(false);
        jButtonIniciar.setEnabled(true);
        camara.release();
    }//GEN-LAST:event_jButtonDetenerActionPerformed

    private void jButtonSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSalirActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jButtonSalirActionPerformed

    private void jTextNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextNombreActionPerformed

    private void jButtonGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGuardarActionPerformed
        // TODO add your handling code here:
        nombrePersona = jTextNombre.getText().replaceAll("\\s+", "");
        crearCarpetaPersona();
        jButtonIniciar.setEnabled(true);
    }//GEN-LAST:event_jButtonGuardarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(JFrameVideo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFrameVideo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFrameVideo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFrameVideo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JFrameVideo().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonDetener;
    private javax.swing.JButton jButtonGuardar;
    private javax.swing.JButton jButtonIniciar;
    private javax.swing.JButton jButtonSalir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextNombre;
    // End of variables declaration//GEN-END:variables
}
