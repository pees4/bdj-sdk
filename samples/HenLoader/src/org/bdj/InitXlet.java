package org.bdj;

import java.io.*;
import java.util.*;
import javax.tv.xlet.*;
import java.awt.BorderLayout;
import org.havi.ui.HScene;
import org.havi.ui.HSceneFactory;
import org.dvb.event.UserEvent;
import org.dvb.event.EventManager;
import org.dvb.event.UserEventListener;
import org.dvb.event.UserEventRepository;
import org.bluray.ui.event.HRcEvent;
import org.bdj.sandbox.DisableSecurityManagerAction;
import org.bdj.external.*;

public class InitXlet implements Xlet, UserEventListener
{
    public static final int BUTTON_X = 10;
    public static final int BUTTON_O = 19;
    public static final int BUTTON_U = 38;
    public static final int BUTTON_D = 40;

    private static InitXlet instance;
    
    public static class EventQueue
    {
        private LinkedList l;
        int cnt = 0;
        EventQueue()
        {
            l = new LinkedList();
        }
        public synchronized void put(Object obj)
        {
            l.addLast(obj);
            cnt++;
        }
        public synchronized Object get()
        {
            if(cnt == 0)
                return null;
            Object o = l.getFirst();
            l.removeFirst();
            cnt--;
            return o;
        }
    }

    private EventQueue eq;
    private HScene scene;
    private Screen gui;
    private XletContext context;
    private static PrintStream console;
    private static final ArrayList messages = new ArrayList();

    public void initXlet(XletContext context)
    {
        // Privilege escalation
        try {
            DisableSecurityManagerAction.execute();
        } catch (Exception e) {}

        instance = this;
        this.context = context;
        this.eq = new EventQueue();
        scene = HSceneFactory.getInstance().getDefaultHScene();

        try
        {
            gui = new Screen(messages);
            gui.setSize(1920, 1080);
            scene.add(gui, BorderLayout.CENTER);

            UserEventRepository repo = new UserEventRepository("input");
            repo.addKey(BUTTON_X);
            repo.addKey(BUTTON_O);
            repo.addKey(BUTTON_U);
            repo.addKey(BUTTON_D);
            EventManager.getInstance().addUserEventListener(this, repo);

            (new Thread()
            {
                public void run()
                {
                    try
                    {
                        scene.repaint();
                        console = new PrintStream(new MessagesOutputStream(messages, scene));

                        console.println("HenLoader v1.1 Modified");
                        console.println("- GoldHEN by SiSTR0");
                        console.println("- Poops by Theflow0");
                        console.println("- Lapse by Gezine");
                        console.println("- BDJ environment by Kimariin");
                        console.println("- Java console by Sleirsgoevy");
                        console.println("");
                        System.gc();

                        if (System.getSecurityManager() != null) {
                            console.println("Privilege escalation gagal! Firmware tidak didukung?");
                            return;
                        }

                        Kernel.initializeKernelOffsets();
                        String fw = Helper.getCurrentFirmwareVersion();
                        console.println("Firmware: " + fw);

                        if (!KernelOffset.hasPS4Offsets())
                        {
                            console.println("Firmware ini belum didukung oleh offset yang ada.");
                            return;
                        }

                        boolean isPoopsFW = fw.equals("12.50") || fw.equals("12.52");

                        while (true)
                        {
                            console.println("\nTekan X atau O untuk melanjutkan...");
                            int key = 0;

                            // Tunggu user tekan X atau O
                            while (key != BUTTON_X && key != BUTTON_O)
                            {
                                key = pollInput();
                                Thread.yield();
                            }

                            console.println("\nMenjalankan exploit...");

                            if (isPoopsFW)
                            {
                                // === POOPS (hanya sekali) ===
                                int result = org.bdj.external.Poops.main(console);
                                if (result == 0)
                                {
                                    console.println("\nBerhasil !\n\nSelamat menikmati game backup-an Anda.");
                                    break;
                                }
                                else
                                {
                                    console.println("\nGagal (kode: " + result + ")");
                                    console.println("\nTekan tombol PS, Restart PS4 dan coba lagi.");
                                    break;
                                }
                            }
                            else
                            {
                                // === LAPSE (bisa retry) ===
                                int attempt = 0;
                                boolean success = false;

                                while (!success && attempt < 3) // maksimal 3 kali retry
                                {
                                    attempt++;
                                    console.println("Percobaan #" + attempt);

                                    int result = org.bdj.external.Lapse.main(console);

                                    if (result == 0)
                                    {
                                        console.println("\nBerhasil !\n\nSelamat menikmati game backup-an Anda.");
                                        success = true;
                                        break;
                                    }
                                    else if (result <= -6)
                                    {
                                        console.println("\nGagal, (kode: " + result + ")");
                                        console.println("\nExploit tidak bisa dilanjutkan, tekan tombol PS, Restart PS4 dan coba lagi.");
                                        break;
                                    }
                                    else
                                    {
                                        console.println("\nGagal, (kode: " + result + "), tapi Anda bisa mencoba lagi...");
                                        console.println("\nTekan X atau O untuk mencoba lagi...");

                                        key = 0;
                                        while (key != BUTTON_X && key != BUTTON_O)
                                        {
                                            key = pollInput();
                                            Thread.yield();
                                        }
                                        // console.println("Percobaan ke: " + attempt);
                                    }
                                }

                                if (success) break;

                                console.println("\nTerlalu banyak kegagalan.\nKeluarkan BD-JB, masukkan lagi, lalu coba lagi.");
                                break;
                            }
                        }
                    }
                    catch(Throwable e)
                    {
                        if (console != null) {
                            console.println("Exception: " + e.toString());
                            printStackTrace(e);
                        }
                        scene.repaint();
                    }
                }
            }).start();
        }
        catch(Throwable e)
        {
            printStackTrace(e);
        }

        scene.validate();
    }

    public void startXlet()
    {
        gui.setVisible(true);
        scene.setVisible(true);
        gui.requestFocus();
    }

    public void pauseXlet()
    {
        gui.setVisible(false);
    }

    public void destroyXlet(boolean unconditional)
    {
        if (scene != null) {
            scene.remove(gui);
            scene = null;
        }
    }

    private void printStackTrace(Throwable e)
    {
        if (console == null) return;
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        console.print(sw.toString());
    }

    public void userEventReceived(UserEvent evt)
    {
        if(evt.getType() == HRcEvent.KEY_PRESSED)
        {
            int code = evt.getCode();

            if(code == BUTTON_U)
                gui.top += 270;
            else if(code == BUTTON_D)
                gui.top -= 270;
            else if(code == BUTTON_X || code == BUTTON_O)
                eq.put(new Integer(code));

            scene.repaint();
        }
    }

    public static void repaint()
    {
        if (instance != null && instance.scene != null)
            instance.scene.repaint();
    }

    public static int pollInput()
    {
        if (instance == null) return 0;
        Object ans = instance.eq.get();
        if(ans == null) return 0;
        return ((Integer)ans).intValue();
    }
}