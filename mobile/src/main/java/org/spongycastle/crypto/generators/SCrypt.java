package org.spongycastle.crypto.generators;

import android.util.Log;

import org.spongycastle.crypto.PBEParametersGenerator;
import org.spongycastle.crypto.digests.SHA256Digest;
import org.spongycastle.crypto.engines.Salsa20Engine;
import org.spongycastle.crypto.params.KeyParameter;
import org.spongycastle.util.Arrays;
import org.spongycastle.util.Pack;

public class SCrypt
{
    // TODO Validate arguments
    public static byte[] generate(byte[] P, byte[] S, int N, int r, int p, int dkLen)
    {
        return MFcrypt(P, S, N, r, p, dkLen);
    }

    private static byte[] MFcrypt(byte[] P, byte[] S, int N, int r, int p, int dkLen)
    {
        int MFLenBytes = r * 128;
        byte[] bytes = SingleIterationPBKDF2(P, S, p * MFLenBytes);

        int[] B = null;

        try
        {
            int BLen = bytes.length >>> 2;
            B = new int[BLen];

            Pack.littleEndianToInt(bytes, 0, B);

            int MFLenWords = MFLenBytes >>> 2;
            Log.w("MFcrypt", "BLen = " + BLen + " MFLenWords = " + MFLenWords);
            System.gc();
            for (int BOff = 0; BOff < BLen; BOff += MFLenWords)
            {
                // TODO These can be done in parallel threads
                SMix(B, BOff, N, r);
            }

            Pack.intToLittleEndian(B, bytes, 0);

            return SingleIterationPBKDF2(P, bytes, dkLen);
        }
        finally
        {
            Clear(bytes);
            Clear(B);
            System.gc();
        }
    }

    private static byte[] SingleIterationPBKDF2(byte[] P, byte[] S, int dkLen)
    {
        PBEParametersGenerator pGen = new PKCS5S2ParametersGenerator(new SHA256Digest());
        pGen.init(P, S, 1);
        KeyParameter key = (KeyParameter) pGen.generateDerivedMacParameters(dkLen * 8);
        return key.getKey();
    }

    private static void SMix(int[] B, int BOff, int N, int r)
    {
        System.gc();
        int BCount = r * 32;

        int[] blockX1 = new int[16];
        int[] blockX2 = new int[16];
        int[] blockY = new int[BCount];

        int[] X = new int[BCount];
        int[][] V = new int[N][];

        try
        {
            System.arraycopy(B, BOff, X, 0, BCount);

            for (int i = 0; i < N; ++i)
            {
                V[i] = Arrays.clone(X);
                BlockMix(X, blockX1, blockX2, blockY, r);
            }

            int mask = N - 1;
            for (int i = 0; i < N; ++i)
            {
                int j = X[BCount - 16] & mask;
                Xor(X, V[j], 0, X);
                Clear(V[j]);
                BlockMix(X, blockX1, blockX2, blockY, r);
                System.gc();
            }
            Clear(blockX1);
            Clear(blockX2);
            Clear(blockY);
            System.gc();
            System.arraycopy(X, 0, B, BOff, BCount);
        }
        finally
        {
            Clear(X);
        }
    }

    private static void BlockMix(int[] B, int[] X1, int[] X2, int[] Y, int r)
    {
        System.arraycopy(B, B.length - 16, X1, 0, 16);

        int BOff = 0, YOff = 0, halfLen = B.length >>> 1;

        for (int i = 2 * r; i > 0; --i)
        {
            Xor(X1, B, BOff, X2);

            Salsa20Engine.salsaCore(8, X2, X1);
            System.arraycopy(X1, 0, Y, YOff, 16);

            YOff = halfLen + BOff - YOff;
            BOff += 16;
        }

        System.arraycopy(Y, 0, B, 0, Y.length);
    }

    private static void Xor(int[] a, int[] b, int bOff, int[] output)
    {
        for (int i = output.length - 1; i >= 0; --i)
        {
            output[i] = a[i] ^ b[bOff + i];
        }
        //Clear(a);
        //Clear(b);
    }

    private static void Clear(byte[] array)
    {
        if (array != null)
        {
            Arrays.fill(array, (byte)0);
            array = null;
            System.gc();
        }

    }

    private static void Clear(int[] array)
    {
        if (array != null)
        {
            Arrays.fill(array, 0);
            array = null;
            System.gc();
        }
    }

    private static void ClearAll(int[][] arrays)
    {
        for (int i = 0; i < arrays.length; i++)
        {
            Clear(arrays[i]);
        }

    }
}
