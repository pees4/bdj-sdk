package org.bdj;
import java.io.*;
import java.util.*;
import java.net.*;
import javax.tv.xlet.*;
import java.awt.BorderLayout;
import org.havi.ui.HScene;
import org.havi.ui.HSceneFactory;
import org.dvb.event.*;
import org.bluray.ui.event.HRcEvent;
import org.bdj.sandbox.DisableSecurityManagerAction;
import org.bdj.external.*;

public class InitXlet implements Xlet, UserEventListener
{
    public static final int BUTTON_X = 10;
    public static final int BUTTON_O = 19;
    private static InitXlet instance;
    
    public static class EventQueue
    {
        private LinkedList list = new LinkedList();
        public synchronized void put(Integer i) { list.addLast(i); }
        public synchronized Integer get()
        {
            return list.isEmpty() ? null : (Integer)list.removeFirst();
        }
    }
    
    private EventQueue eq = new EventQueue();
    private HScene scene;
    private Screen gui;
    private XletContext context;
    private static PrintStream console;
    private static final ArrayList messages = new ArrayList();

    public void initXlet(XletContext ctx)
    {
        try { DisableSecurityManagerAction.execute(); } catch (Exception ignored) {}
        instance = this;
        this.context = ctx;
        scene = HSceneFactory.getInstance().getDefaultHScene();
        
        try
        {
            gui = new Screen(messages);
            gui.setSize(1920, 1080);
            scene.add(gui, BorderLayout.CENTER);

            // Tidak perlu register tombol lagi karena sudah full auto
            // UserEventRepository repo = new UserEventRepository("bdj");
            // repo.addKey(BUTTON_X);
            // repo.addKey(BUTTON_O);
            // EventManager.getInstance().addUserEventListener(this, repo);

            new Thread(new Runnable() {
                public void run() {
                    runExploitThread();
                }
            }).start();
        }
        catch (Throwable t)
        {
            printStackTrace(t);
        }
        
        scene.validate();
        scene.setVisible(true);
    }

    private void runExploitThread()
    {
        try
        {
            scene.repaint();
            console = new PrintStream(new MessagesOutputStream(messages, scene));
            console.println("");
            console.println("HenLoader by 15432, MOD by CM");
			console.println("GoldHEN by SiSTR0");
			console.println("Poops by Theflow0");
			console.println("Lapse by abc, MOD by Gezine");
            console.println("BDJ environment by Kimariin");
            console.println("Java console by Sleirsgoevy");
            console.println("");
            
            System.gc();
            Socket s = new Socket();
            byte[][] byteArrays = new byte[100][];
            for (int i = 0; i < 100; i++) {
                int size = 0;
                if (i < 20)
                    size = 0x100;
                else if (i < 40)
                    size = 0x200;
                else if (i < 60)
                    size = 0x400;
                else
                    size = 0x1000;
                byteArrays[i] = new byte[size];
            }

            if (System.getSecurityManager() != null)
            {
                console.println("Privilege escalation gagal!\nUntuk FW >=13.00, copy 00000.jar ke /system_ex/app/NPXS20113/bdjstack/lib/ext PS4");
                return;
            }
            
            Kernel.initializeKernelOffsets();
            String fw = Helper.getCurrentFirmwareVersion();
            console.println("Firmware PS4 ini: " + fw);
            
            if (!KernelOffset.hasPS4Offsets())
            {
                console.println("BD-JB tidak support firmware PS4 ini\nPastikan firmware PS4 Anda antara 9.00-13.00.");
                return;
            }
            
            boolean lapseSupported = (!fw.equals("12.50") && !fw.equals("12.52") && !fw.equals("13.00"));

            // Jeda 2 detik sebelum mulai exploit (biar user sempat baca pesan)
            // console.println("\nExploit akan dimulai otomatis dalam 2 detik...");
            // Thread.sleep(1000);

            console.println("\nMenjalankan exploit...");
            final int MAX_ATTEMPTS = 2;
            int attempt = 0;
            boolean success = false;
            boolean fatalError = false;

            while (!success && attempt < MAX_ATTEMPTS && !fatalError)
            {
                attempt++;
                console.println("\nPercobaan #" + attempt);
                
                int result = lapseSupported
                    ? org.bdj.external.Lapse.main(console)
                    : org.bdj.external.Poops.main(console);
                    
                if (result == 0)
                {
                    console.println("\nBerhasil!\nSelamat menikmati game backup Anda.\n\nTahan tombol PS > Close Application/Tutup Aplikasi > OK");
                    success = true;
                }
                else if (result <= -6)
                {
                    // console.println("\nGagal (kode: " + result + ")\nTahan tombol PS > Power/Daya > Restart PS4/Mulai Ulang PS4, dan coba lagi.");
					console.println("\nGagal (kode: " + result + ")\nRestart PS4, dan coba lagi.");
                    fatalError = true;
                }
                else
                {
                    console.println("Gagal (kode: " + result + "), mencoba ulang...");
                    Thread.sleep(1500); // jeda sebelum coba lagi
                }
            }
            
            if (!success && !fatalError)
            {
                console.println("\nSudah " + MAX_ATTEMPTS + " kali gagal dengan kode yang sama");
                // console.println("Tahan tombol PS > Power/Daya > Restart PS4/Mulai Ulang PS4, dan coba lagi.");
				console.println("Restart PS4, dan coba lagi.");
            }
        }
        catch (Throwable t)
        {
            if (console != null)
            {
                console.println("Exception: " + t);
                printStackTrace(t);
            }
        }
        finally
        {
            scene.repaint();
        }
    }

    public void startXlet()
    {
        if (gui != null) gui.setVisible(true);
        if (scene != null) scene.setVisible(true);
        if (gui != null) gui.requestFocus();
    }

    public void pauseXlet()
    {
        if (gui != null) gui.setVisible(false);
    }

    public void destroyXlet(boolean unconditional)
    {
        if (scene != null)
        {
            scene.remove(gui);
            scene.setVisible(false);
            scene = null;
        }
        try {
            EventManager.getInstance().removeUserEventListener(this);
        } catch (Exception ignored) {}
    }

    // Tetap dibiarkan kosong karena sudah tidak dipakai lagi
    public void userEventReceived(UserEvent e) { }

    public static void repaint()
    {
        if (instance != null && instance.scene != null)
            instance.scene.repaint();
    }

    public static int pollInput()
    {
        return 0; // tidak dipakai lagi
    }

    private void printStackTrace(Throwable t)
    {
        if (console == null) return;
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        console.print(sw.toString());
    }
}