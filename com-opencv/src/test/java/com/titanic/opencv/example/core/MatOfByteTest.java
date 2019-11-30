package com.titanic.opencv.example.core;

import com.titanic.opencv.example.OpenCVTestCase;
import org.opencv.core.MatOfByte;

public class MatOfByteTest extends OpenCVTestCase
{

    public void testMatOfSubByteArray() {
        byte[] inputBytes = { 1,2,3,4,5 };

        MatOfByte m0 = new MatOfByte(inputBytes);
        MatOfByte m1 = new MatOfByte(0, inputBytes.length, inputBytes);
        MatOfByte m2 = new MatOfByte(1, inputBytes.length - 2, inputBytes);

        assertEquals(5.0, m0.size().height);
        assertEquals(1.0, m0.size().width);

        assertEquals(m0.get(0, 0)[0], m1.get(0, 0)[0]);
        assertEquals(m0.get((int) m0.size().height - 1, 0)[0], m1.get((int) m1.size().height - 1, 0)[0]);

        assertEquals(3.0, m2.size().height);
        assertEquals(1.0, m2.size().width);

        assertEquals(2.0, m2.get(0, 0)[0]);
        assertEquals(3.0, m2.get(1, 0)[0]);
        assertEquals(4.0, m2.get(2, 0)[0]);
    }


    public void testMatOfSubByteArray_BadArg() {
        byte[] inputBytes = { 1,2,3,4,5 };

        try {
            MatOfByte m1 = new MatOfByte(-1, inputBytes.length, inputBytes);
            fail("Missing check: offset < 0");
        } catch (IllegalArgumentException e) {
            // pass
        }

        try {
            MatOfByte m1 = new MatOfByte(0, inputBytes.length, null);
            fail("Missing check: NullPointerException");
        } catch (NullPointerException e) {
            // pass
        }

        try {
            MatOfByte m1 = new MatOfByte(0, -1, inputBytes);
            fail("Missing check: length < 0");
        } catch (IllegalArgumentException e) {
            // pass
        }

        try {
            MatOfByte m1 = new MatOfByte(1, inputBytes.length, inputBytes);
            fail("Missing check: buffer bounds");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

}
