/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.picklecode.popflix;

import com.picklecode.popflix.torrent.TorrentStreamService;
import com.picklecode.popflix.torrent.TorrentServer;
import com.picklecode.popflix.upnp.UpnpPlayService;
import com.picklecode.popflix.upnp.UpnpDevice;
import com.picklecode.popflix.upnp.UpnpSearchListener;
import com.picklecode.popflix.upnp.UpnpSearchService;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.Map;
import java.util.logging.Level;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.tika.Tika;
import org.nikkii.embedhttp.HttpServer;
import org.nikkii.embedhttp.handler.HttpRequestHandler;
import org.nikkii.embedhttp.impl.HttpMethod;
import org.nikkii.embedhttp.impl.HttpRequest;
import org.nikkii.embedhttp.impl.HttpResponse;
import org.nikkii.embedhttp.impl.HttpStatus;
import static org.nikkii.embedhttp.impl.HttpStatus.PARTIAL_CONTENT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import torrentstream.StreamStatus;
import torrentstream.Torrent;
import torrentstream.listeners.TorrentListener;

/**
 *
 * @author bruno
 */
public class Popflix extends javax.swing.JFrame implements UpnpSearchListener, TorrentListener {

    private static final Logger LOG = LoggerFactory.getLogger(Popflix.class);
    private static String OS = System.getProperty("os.name").toLowerCase();
    public static String ARCH = System.getProperty("os.arch").toLowerCase();

    UpnpSearchService upnpSearch;
    TorrentStreamService streamService;
    private int lastStatus;
    HttpServer server;
    private UpnpDevice selectedDevice;
    private boolean mute;

    /**
     * Creates new form PopFlix
     */
    public Popflix() {
        initComponents();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                if (server != null) {
                    server.stop();
                    streamService.stop();
                    if (selectedDevice != null) {
                        new UpnpPlayService(selectedDevice).stop();
                    }
                }
            }
        });
        playBtn.setEnabled(false);
        jButton1.setEnabled(false);
        deviceList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                playBtn.setEnabled(deviceList.getSelectedValue() != null && torrentTxt.getText().trim().length() > 0);
                selectedDevice = upnpSearch.getDevice(deviceList.getSelectedValue());
            }
        });

        torrentTxt.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                playBtn.setEnabled(deviceList.getSelectedValue() != null && torrentTxt.getText().trim().length() > 0);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                playBtn.setEnabled(deviceList.getSelectedValue() != null && torrentTxt.getText().trim().length() > 0);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                playBtn.setEnabled(deviceList.getSelectedValue() != null && torrentTxt.getText().trim().length() > 0);
            }
        });
        streamService = new TorrentStreamService();
        upnpSearch = new UpnpSearchService();
        upnpSearch.addUpnpSearchListener(this);
        upnpSearch.start();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        deviceList = new javax.swing.JList<>();
        jLabel1 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        torrentTxt = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        playBtn = new javax.swing.JButton();
        jProgressBar1 = new javax.swing.JProgressBar();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Popflix");
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("logo.png")));
        setResizable(false);

        deviceList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane3.setViewportView(deviceList);

        jLabel1.setText("Devices :");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                .addContainerGap())
        );

        torrentTxt.setColumns(20);
        torrentTxt.setLineWrap(true);
        torrentTxt.setRows(5);
        torrentTxt.setWrapStyleWord(true);
        jScrollPane1.setViewportView(torrentTxt);

        jLabel3.setText("Torrent :");

        playBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/picklecode/popflix/cast.png"))); // NOI18N
        playBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playBtnActionPerformed(evt);
            }
        });

        jProgressBar1.setStringPainted(true);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/picklecode/popflix/stop.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(playBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1))
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(playBtn)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void playBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playBtnActionPerformed

        new Thread() {
            @Override
            public void run() {
                streamService.download(torrentTxt.getText().trim(), Popflix.this);
            }
        }.start();
    }//GEN-LAST:event_playBtnActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (selectedDevice != null) {
            new UpnpPlayService(selectedDevice).stop();
            streamService.stop();
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    static {
        try {
            loadLib("libjlibtorrent");
        } catch (UnsatisfiedLinkError e) {
            LOG.error(e.getMessage());
            System.exit(1);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        try {

            javax.swing.UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Popflix.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Popflix().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList<String> deviceList;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JButton playBtn;
    private javax.swing.JTextArea torrentTxt;
    // End of variables declaration//GEN-END:variables

    @Override
    public void onDeviceFound(UpnpDevice d) {
        deviceList.setListData(upnpSearch.getDevicesNames());
    }

    @Override
    public void onStreamPrepared(Torrent torrent) {
        LOG.info("Stream Prepared");
        torrent.startDownload();
        lastStatus = 0;
    }

    @Override
    public void onStreamStarted(Torrent torrent) {
        LOG.info("Stream Started");

    }

    @Override
    public void onStreamError(Torrent torrent, Exception e) {
        LOG.info("Stream Error", e);

    }

    private HttpResponse getPartialResponse(String mimeType, String rangeHeader, File file) throws IOException {

        String rangeValue = rangeHeader.trim().substring("bytes=".length());
        long fileLength = file.length();
        long start, end;
        if (rangeValue.startsWith("-")) {
            end = fileLength - 1;
            start = fileLength - 1
                    - Long.parseLong(rangeValue.substring("-".length()));
        } else {
            String[] range = rangeValue.split("-");
            start = Long.parseLong(range[0]);
            end = range.length > 1 ? Long.parseLong(range[1])
                    : fileLength - 1;
        }
        if (end > fileLength - 1) {
            end = fileLength - 1;
        }
        if (start <= end) {
            long contentLength = end - start + 1;
            FileInputStream fileInputStream = new FileInputStream(file);
            //noinspection ResultOfMethodCallIgnored
            fileInputStream.skip(start);
            HttpResponse response = new HttpResponse(PARTIAL_CONTENT, fileInputStream);
            response.addHeader("Content-Length", contentLength + "");
            response.addHeader("Content-Range", "bytes " + start + "-" + end + "/" + fileLength);
            response.addHeader("Content-Type", mimeType);
            response.addHeader("Accept-Ranges", "bytes");
            response.addHeader("transferMode.dlna.org", "Streaming");
            response.addHeader("contentFeatures.dlna.org", "DLNA.ORG_OP=01;DLNA.ORG_CI=0;DLNA.ORG_FLAGS=017000 00000000000000000000000000");

            return response;
        } else {
            return new HttpResponse(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE, rangeHeader);
        }
    }

    @Override
    public void onStreamReady(Torrent torrent) {

        if (selectedDevice != null) {
            try {
                if (server != null) {
                    server.stop();
                }

                server = new TorrentServer(0);

                server.addRequestHandler(new HttpRequestHandler() {

                    @Override
                    public HttpResponse handleRequest(HttpRequest request) {
                        try {
                            if (request.getMethod() == HttpMethod.HEAD) {
                                HttpResponse response = new HttpResponse(HttpStatus.OK);
                                response.addHeader("Accept-Ranges", "bytes");
                                response.addHeader("Content-Type", getMime(torrent.getVideoFile()));
                                response.addHeader("transferMode.dlna.org", "Streaming");
                                response.addHeader("contentFeatures.dlna.org", "DLNA.ORG_OP=01;DLNA.ORG_CI=0;DLNA.ORG_FLAGS=017000 00000000000000000000000000");

                                return response;
                            }

                            Map<String, String> headers = request.getHeaders();

                            String range = null;
                            for (String key : headers.keySet()) {
                                if ("range".equalsIgnoreCase(key)) {
                                    range = headers.get(key);
                                }
                            }
                            try {
                                if (range == null) {
                                    HttpResponse response = new HttpResponse(HttpStatus.OK, new FileInputStream(torrent.getVideoFile()));
                                    response.addHeader("Content-Type", getMime(torrent.getVideoFile()));
                                    response.addHeader("transferMode.dlna.org", "Streaming");
                                    response.addHeader("contentFeatures.dlna.org", "DLNA.ORG_OP=01;DLNA.ORG_CI=0;DLNA.ORG_FLAGS=017000 00000000000000000000000000");

                                    return response;
                                } else {
                                    return getPartialResponse(getMime(torrent.getVideoFile()), range, torrent.getVideoFile());
                                }
                            } catch (IOException e) {
                                LOG.error(e.getMessage());
                            }
                        } catch (Exception e) {
                            LOG.error(e.getMessage());
                        }

                        return new HttpResponse(HttpStatus.NOT_FOUND);

                    }
                });

                server.start();
                jButton1.setEnabled(true);

                UpnpPlayService service = new UpnpPlayService(selectedDevice);
                service.stop();
                service.setAVTransportURI("http://" + getLocalHostLANAddress().getHostAddress() + ":" + server.getPort());
                service.play();

            } catch (IOException ex) {
                LOG.error(ex.getMessage());
            }
        } else {
            streamService.stop();

        }
    }

    @Override
    public void onStreamProgress(Torrent torrent, StreamStatus status) {

        if (lastStatus != (int) status.progress) {
            lastStatus = (int) status.progress;
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    jProgressBar1.setValue((int) status.progress);
                }
            });
        }

    }

    @Override
    public void onStreamStopped() {
        LOG.info("Stream Stopped");
        jProgressBar1.setValue(0);
        if (server != null) {
            server.stop();
            jButton1.setEnabled(false);
        }

    }

    /////MOVEE
    private static InetAddress getLocalHostLANAddress() throws UnknownHostException {
        try {
            InetAddress candidateAddress = null;
            // Iterate all NICs (network interface cards)...
            for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements();) {
                NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
                // Iterate all IP addresses assigned to each card...
                for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements();) {
                    InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
                    if (!inetAddr.isLoopbackAddress()) {

                        if (inetAddr.isSiteLocalAddress()) {
                            // Found non-loopback site-local address. Return it immediately...
                            return inetAddr;
                        } else if (candidateAddress == null) {
                            // Found non-loopback address, but not necessarily site-local.
                            // Store it as a candidate to be returned if site-local address is not subsequently found...
                            candidateAddress = inetAddr;
                            // Note that we don't repeatedly assign non-loopback non-site-local addresses as candidates,
                            // only the first. For subsequent iterations, candidate will be non-null.
                        }
                    }
                }
            }
            if (candidateAddress != null) {
                // We did not find a site-local address, but we found some other non-loopback address.
                // Server might have a non-site-local address assigned to its NIC (or it might be running
                // IPv6 which deprecates the "site-local" concept).
                // Return this non-loopback candidate address...
                return candidateAddress;
            }
            // At this point, we did not find a non-loopback address.
            // Fall back to returning whatever InetAddress.getLocalHost() returns...
            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
            if (jdkSuppliedAddress == null) {
                throw new UnknownHostException("The JDK InetAddress.getLocalHost() method unexpectedly returned null.");
            }
            return jdkSuppliedAddress;
        } catch (SocketException | UnknownHostException e) {
            UnknownHostException unknownHostException = new UnknownHostException("Failed to determine LAN address: " + e);
            unknownHostException.initCause(e);
            throw unknownHostException;
        }
    }
    private final Tika tika = new Tika();

    public String getMime(File f) throws IOException {
        return tika.detect(f);
    }

    private static void loadLib(String name) {

        try {

            if (isWindows()) {
                name = name.substring("lib".length());
            }
            
            String ext = getExtension();
            name = name + ext;

            LOG.info(System.getProperty("os.arch"));
            LOG.info(System.getProperty("os.name"));
            Path tmp = Files.createTempDirectory("popflix");
            setLibraryPath(tmp.toString());
            LOG.info(tmp.toString() + "/" + name);
            File fileOut = new File(tmp.toString() + "/" + name);

            LOG.info(System.getProperty("java.library.path"));

            System.out.println("/lib/" + getFolder() + "/" + name);
            InputStream in = Popflix.class.getResourceAsStream("/lib/" + getFolder() + "/" + name);
            if (in != null) {

                OutputStream out = FileUtils.openOutputStream(fileOut);
                IOUtils.copy(in, out);
                in.close();
                out.close();

            }
            System.load(fileOut.getAbsolutePath());//loading goes here
        } catch (Exception e) {
            LOG.error(e.getMessage());
            System.exit(-1);
        }
    }

    public static boolean isWindows() {

        return (OS.indexOf("win") >= 0);

    }

    public static boolean isMac() {

        return (OS.indexOf("mac") >= 0);

    }

    public static boolean isUnix64() {

        return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0) && ARCH.indexOf("64") >= 0;

    }

    public static boolean isUnix() {

        return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0) && ARCH.indexOf("64") == -1;

    }

    private static String getExtension() {
        if (isWindows()) {
            return ".dll";
        }

        if (isUnix() || isUnix64()) {
            return ".so";
        }

        if (isMac()) {
            return ".dylib";
        }

        return "";
    }

    private static String getFolder() {
        if (isUnix64()) {
            return "x86_64";
        }

        if (isUnix() || isWindows()) {
            return "x86";
        }

        return "";
    }

    public static void setLibraryPath(String path) throws Exception {
        System.setProperty("java.library.path", path);

        //set sys_paths to null so that java.library.path will be reevalueted next time it is needed
        final Field sysPathsField = ClassLoader.class.getDeclaredField("sys_paths");
        sysPathsField.setAccessible(true);
        sysPathsField.set(null, null);
    }

}