package com.jm.wf.common.utils;

import java.nio.ByteBuffer;

/**
 * Utility functions for handling unsigned data in byte buffers. 
 */
public class UnsignedInts 
{
    // unsigned bytes

    public static short getUnsignedByte(ByteBuffer bb)
    {
        return ((short) (bb.get() & 0xFF));
    }

    public static void putUnsignedByte(ByteBuffer bb, int value)
    {
        bb.put((byte) (value & 0xFF));
    }

    public static short getUnsignedByte(ByteBuffer bb, int position)
    {
        return ((short) (bb.get(position) & (short) 0xFF));
    }

    public static void putUnsignedByte(ByteBuffer bb, int position, int value)
    {
        bb.put(position, (byte) (value & 0xFF));
    }

    // unsigned shorts

    public static int getUnsignedShort(ByteBuffer bb)
    {
        return (bb.getShort() & 0xFFFF);
    }

    public static void putUnsignedShort(ByteBuffer bb, int value)
    {
        bb.putShort((short) (value & 0xFFFF));
    }

    public static int getUnsignedShort(ByteBuffer bb, int position)
    {
        return (bb.getShort(position) & 0xFFFF);
    }

    public static void putUnsignedShort(ByteBuffer bb, int position, int value)
    {
        bb.putShort(position, (short) (value & 0xFFFF));
    }

    // unsigned ints

    public static long getUnsignedInt(ByteBuffer bb)
    {
        return ((long) bb.getInt() & 0xFFFFFFFFL);
    }

    public static void putUnsignedInt(ByteBuffer bb, long value)
    {
        bb.putInt((int) (value & 0xFFFFFFFFL));
    }

    public static long getUnsignedInt(ByteBuffer bb, int position)
    {
        return ((long) bb.getInt(position) & 0xFFFFFFFFL);
    }

    public static void putUnsignedInt(ByteBuffer bb, int position, long value)
    {
        bb.putInt(position, (int) (value & 0xFFFFFFFFL));
    }

    // unsigned longs

    public static long getUnsignedLong(ByteBuffer bb)
    {
        return ((long) bb.getLong() & 0xFFFFFFFFFFFFFFFFL);
    }

    public static void putUnsignedLong(ByteBuffer bb, long value)
    {
        bb.putLong((long) (value & 0xFFFFFFFFFFFFFFFFL));
    }

    public static long getUnsignedLong(ByteBuffer bb, int position)
    {
        return ((long) bb.getLong(position) & 0xFFFFFFFFFFFFFFFFL);
    }

    public static void putUnsignedLong(ByteBuffer bb, int position, long value)
    {
        bb.putLong(position, (long) (value & 0xFFFFFFFFFFFFFFFFL));
    }

    // unexplained extra putUnsignedShort that takes a long parameter

    public static void putUnsignedShort(ByteBuffer bb, int position, long value)
    {
        bb.putInt(position, (int) (value & 0xFFFFFFFFL));
    }
}

/*** END OF FILE ***/

