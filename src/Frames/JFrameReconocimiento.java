/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Frames;

import DAO.IUserImp;
import static Frames.JFrameVideo.fMenu;
import Modelos.User;
import com.demo.utils.Utils;
import java.awt.Color;
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
import org.opencv.face.EigenFaceRecognizer;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.opencv.videoio.VideoCapture;

/**
 *
 * @author jdanielh
 */
public class JFrameReconocimiento extends javax.swing.JFrame {

    static FrameMenu fm;
    String rutaBase = System.getProperty("user.dir");
    String rutaClasificador = rutaBase + "/recursos/DeteccionFacial/haarcascade_frontalface_alt.xml";
    String rutaPersona = rutaBase;
    String nombrePersona;

    VideoThread videoThread = null;
    VideoCapture camara = null;
    Mat imgMatriz = new Mat();
    CascadeClassifier detectorRostros = new CascadeClassifier(rutaClasificador);
    MatOfRect rostrosDetectados = new MatOfRect();
    MatOfByte mem = new MatOfByte();
    
    EigenFaceRecognizer efr  = null;
    String[] listaPersonas = null;
    
    class VideoThread implements Runnable {

        protected volatile boolean runnable = false;

        @Override
        public void run() {
            synchronized (this) {
                while (runnable) {
                    if (camara.grab()) {
                        try {
                            camara.retrieve(imgMatriz);
                            Graphics g = jPanel1.getGraphics();
                            
                            Mat imgMatrizGrises = new Mat();
                            int tama??oAbsolutoRostro = 0;

                            // Convertir imgMatriz en escala de grises
                            Imgproc.cvtColor(imgMatriz, imgMatrizGrises, Imgproc.COLOR_BGR2GRAY);
                            // Ecualizar el histograma de imgMatriz para mejorar el contraste
                            Imgproc.equalizeHist(imgMatrizGrises, imgMatrizGrises);

                            // Calcular el tama??o minimo del rostro(1% de imgMatriz altura, en nuestro caso)
                            int altura = imgMatrizGrises.rows();
                            if (Math.round(altura * 0.2f) > 0) {
                                tama??oAbsolutoRostro = Math.round(altura * 0.3f);
                            }

                            // detectar rostros
                            detectorRostros.detectMultiScale(imgMatrizGrises, rostrosDetectados, 1.1, 2, 0 | Objdetect.CASCADE_SCALE_IMAGE,
                                    new Size(tama??oAbsolutoRostro, tama??oAbsolutoRostro), new Size(altura, altura));

                            for (Rect rect : rostrosDetectados.toArray()) {
                                //Recortar fotografia del rostro y guardarlo en su carpeta
                                Mat imgRecortada = new Mat(imgMatrizGrises, rect);
                                Mat imgRedimensionada = new Mat();
                                Size sz = new Size(200, 200);
                                Imgproc.resize(imgRecortada, imgRedimensionada, sz);
                                
                                int[] resultadoEtiquetas = new int[1];
                                double[] resultadoConf = new double[1];
                                efr.predict(imgRedimensionada, resultadoEtiquetas, resultadoConf);
                                
                                if(resultadoConf[0] < 6000){
                                    Imgproc.putText(imgMatriz, listaPersonas[resultadoEtiquetas[0]], 
                                            new Point(rect.x, rect.y-20), 2, 1, new Scalar(0, 255, 0));
                                    Imgproc.rectangle(imgMatriz, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                                        new Scalar(0, 255, 0));
                                    ponerDatosUsuario(listaPersonas[resultadoEtiquetas[0]]);
                                }else{
                                   Imgproc.putText(imgMatriz, "No se reconoce", 
                                            new Point(rect.x, rect.y-20), 2, 1, new Scalar(0, 0, 255));
                                    Imgproc.rectangle(imgMatriz, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                                        new Scalar(0, 0, 255)); 
                                }
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
    
    private void ponerDatosUsuario(String id){
        IUserImp iu = new IUserImp();
        User u = iu.getUser(Integer.parseInt(id));
        jLabelID.setText(u.getId()+"");
        jLabelUser.setText(u.getName());
        jLabelJob.setText(u.getJobTitle());
    }
    
    private void cargarModeloEigenFace(){
        File directorioDatos = new File(rutaBase + "/Datos/");
        listaPersonas = directorioDatos.list();
        
        efr = EigenFaceRecognizer.create();
        efr.read("modeloEigenFace.xml");
    }
    
    /**
     * Creates new form JFrameReconocimiento
     */
    public JFrameReconocimiento(FrameMenu fm) {
        initComponents();
        this.getContentPane().setBackground(new Color(15,32,39));
        this.fm = fm;
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
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabelID = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabelUser = new javax.swing.JLabel();
        jLabelJob = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jButtonExit = new javax.swing.JButton();
        jButtonClear = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 800, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 473, Short.MAX_VALUE)
        );

        jButtonIniciar.setBackground(new java.awt.Color(153, 223, 236));
        jButtonIniciar.setFont(new java.awt.Font("Open Sans", 0, 16)); // NOI18N
        jButtonIniciar.setForeground(new java.awt.Color(16, 16, 16));
        jButtonIniciar.setText("Start");
        jButtonIniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonIniciarActionPerformed(evt);
            }
        });

        jButtonDetener.setBackground(new java.awt.Color(153, 223, 236));
        jButtonDetener.setFont(new java.awt.Font("Open Sans", 0, 16)); // NOI18N
        jButtonDetener.setForeground(new java.awt.Color(16, 16, 16));
        jButtonDetener.setText("Stop");
        jButtonDetener.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDetenerActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(15, 32, 39));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2));

        jLabel1.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(254, 254, 254));
        jLabel1.setText("ID:");

        jLabelID.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N
        jLabelID.setForeground(new java.awt.Color(254, 254, 254));

        jLabel3.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(254, 254, 254));
        jLabel3.setText("User:");

        jLabelUser.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N
        jLabelUser.setForeground(new java.awt.Color(254, 254, 254));

        jLabelJob.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N
        jLabelJob.setForeground(new java.awt.Color(254, 254, 254));

        jLabel6.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(254, 254, 254));
        jLabel6.setText("Job Title:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabelID, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabelUser, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabelJob, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jLabelID, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jLabelUser, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel6)
                                .addComponent(jLabelJob, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(32, Short.MAX_VALUE))
        );

        jButtonExit.setBackground(new java.awt.Color(153, 223, 236));
        jButtonExit.setFont(new java.awt.Font("Open Sans", 0, 16)); // NOI18N
        jButtonExit.setForeground(new java.awt.Color(16, 16, 16));
        jButtonExit.setText("Exit");
        jButtonExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExitActionPerformed(evt);
            }
        });

        jButtonClear.setBackground(new java.awt.Color(153, 223, 236));
        jButtonClear.setFont(new java.awt.Font("Open Sans", 0, 16)); // NOI18N
        jButtonClear.setForeground(new java.awt.Color(16, 16, 16));
        jButtonClear.setText("Clear");
        jButtonClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonClearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(90, 90, 90)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButtonIniciar, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonDetener, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonClear, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonExit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(215, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonIniciar, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonDetener, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonExit, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonClear, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonDetenerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDetenerActionPerformed
        // TODO add your handling code here:
        videoThread.runnable = false;
        jButtonDetener.setEnabled(false);
        jButtonIniciar.setEnabled(true);
        camara.release();
    }//GEN-LAST:event_jButtonDetenerActionPerformed

    private void jButtonIniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonIniciarActionPerformed
        // TODO add your handling code here:
        cargarModeloEigenFace();
        camara = new VideoCapture(0); // capturar video de la camara por default
        videoThread = new VideoThread(); 
        Thread t = new Thread(videoThread);
        t.setDaemon(true);
        videoThread.runnable = true;
        t.start();
        jButtonIniciar.setEnabled(false);  // deactivate start button
        jButtonDetener.setEnabled(true);  //  activate stop button
    }//GEN-LAST:event_jButtonIniciarActionPerformed

    private void jButtonExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExitActionPerformed
        // TODO add your handling code here:
        fm.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButtonExitActionPerformed

    private void jButtonClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonClearActionPerformed
        // TODO add your handling code here:
        jLabelID.setText("");
        jLabelUser.setText("");
        jLabelJob.setText("");
    }//GEN-LAST:event_jButtonClearActionPerformed

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
            java.util.logging.Logger.getLogger(JFrameReconocimiento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFrameReconocimiento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFrameReconocimiento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFrameReconocimiento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JFrameReconocimiento(fm).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonClear;
    private javax.swing.JButton jButtonDetener;
    private javax.swing.JButton jButtonExit;
    private javax.swing.JButton jButtonIniciar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabelID;
    private javax.swing.JLabel jLabelJob;
    private javax.swing.JLabel jLabelUser;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
}
