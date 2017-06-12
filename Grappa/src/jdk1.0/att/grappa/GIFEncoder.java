package att.grappa;

import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.image.*;

/**
 * GIFEncoder is a class which takes an image and saves it to a stream
 * using the GIF file format (<A
 * HREF="http://www.dcs.ed.ac.uk/%7Emxr/gfx/">Graphics Interchange
 * Format</A>). A GIFEncoder
 * is constructed with either an AWT Image (which must be fully
 * loaded). The image can be written out with a
 * call to <CODE>Write</CODE>.<P>
 *
 * Three caveats:
 * <UL>
 *   <LI>GIFEncoder will convert the image to indexed color upon
 *   construction. This will take some time, depending on the size of
 *   the image. Also, actually writing the image out (Write) will take
 *   time.<P>
 *
 *   <LI>The image cannot have more than 256 colors, since the encoder uses an 8
 *   bit format. For a 24 bit to 8 bit quantization algorithm, see
 *   Graphics Gems II III.2 by Xialoin Wu. Or check out his <A
 *   HREF="http://www.csd.uwo.ca/faculty/wu/cq.c">C source</A>.<P>
 *
 *   <LI>Since the image must be completely loaded into memory,
 *   GIFEncoder may have problems with large images. Attempting to
 *   encode an image which will not fit into memory will probably
 *   result in the following exception:<P>
 *   <CODE>java.awt.AWTException: Grabber returned false: 192</CODE><P>
 *
 * </UL><P>
 *
 * GIFEncoder is based upon gifsave.c, which was written and released
 * by:<P>
 * <CENTER>
 *                                  Sverre H. Huseby<BR>
 *                                   Bjoelsengt. 17<BR>
 *                                     N-0468 Oslo<BR>
 *                                       Norway<P>
 *
 *                                 Phone: +47 2 230539<BR>
 *                                 sverrehu@ifi.uio.no<P>
 * </CENTER>
 * @version 0.90 21 Apr 1996
 * @author <A HREF="http://www.cs.brown.edu/people/amd/">Adam Doppelt</A>
 *
 * rewrote to go faster - still needs work Steve Barrett
 * sbarrett@dshs.wa.gov
 *
 *   Some attempt made to guard against memory shortages and speed
 *   it up (as well as reduce the number of classes) was made by
 *   john@research.att.com (John Mocenigo) 10 Sep 1997.
 */
 
public class GIFEncoder
{
  private int full_width_, full_height_;
  private int numColors_;
  private byte pixels_[], colors_[];
  //long mystart = System.currentTimeMillis();
    

  public GIFEncoder(Image image) throws AWTException {
    full_width_ = image.getWidth(null);
    full_height_ = image.getHeight(null);

    PixelGrabber grabber = null;

    int SCAN_LINES = full_height_;

    int values[] = null;
    int width_ = full_width_;
    int height_ = -1;

    pixels_ = new byte[full_width_*full_height_];
    colors_ = new byte[256 * 3];
    // try to avoid OutOfMemoryError condition while not slowing
    // things down unnecessarily
    while(height_ < 0) {
      try {
	height_ = SCAN_LINES;
	values = new int[width_*height_];
      } catch(OutOfMemoryError err) {
	if(SCAN_LINES == 1) {
	  throw new OutOfMemoryError("Image width (" + width_ + ") is too big");
	}
	SCAN_LINES /= 2;
	if(SCAN_LINES < 1) SCAN_LINES = 1;
	height_ = -1;
      }
    }

    int start_height_ = 0;


    int noalpha, colornum = 0;
    int idx = -1;
    int newidx = 0;
    int range = -1;

    start_height_ = 0;
    do {
      height_ = ((height_ = (full_height_ - start_height_)) > SCAN_LINES) ? SCAN_LINES : height_;

      grabber = new PixelGrabber(image, 0, start_height_, full_width_, height_, values, 0, full_width_);
   
      try {
	if(grabber.grabPixels() != true)
	  throw new AWTException("Grabber returned false: " + grabber.status());
      }
      catch (InterruptedException e) {};
      
      range = (height_ * full_width_);
      for (int y = 0; y < range; y++) {
	noalpha = values[y] & 0xFFFFFF;
	for(idx=0; idx < newidx; idx+=3) {
	  if(colors_[idx+2] == (byte)noalpha && colors_[idx+1] == (byte)(noalpha >>> 8) && colors_[idx] == (byte)(noalpha >>> 16)) {
	    idx /= 3;
	    break;
	  }
	}

	if(idx >= newidx) {
	  if (colornum == 256)
	    throw new AWTException("Too many colors.");

	  idx = colornum;
	  colors_[newidx] = (byte)(noalpha >>> 16);  // red
	  colors_[++newidx] = (byte)(noalpha >>> 8); // green
	  colors_[++newidx] = (byte)noalpha;         // blue
	  ++colornum;
	  ++newidx;
	}
	pixels_[(start_height_  * full_width_) + y] = (byte)idx;
      }

      start_height_ += SCAN_LINES;
    } while(start_height_ < full_height_);
    //System.err.println("took " + (System.currentTimeMillis()- mystart) + " to grab pixels");
    //mystart = System.currentTimeMillis();   
    numColors_ = 1 << BitsNeeded(colornum);
    byte copy[] = new byte[numColors_ * 3];
    System.arraycopy(colors_, 0, copy, 0, numColors_ * 3);
    colors_ = copy;
  }

  /**
   * Writes the image out to a stream in the GIF file format. This will
   * be a single GIF87a image, non-interlaced, with no background color.
   * */
  public void Write(OutputStream output) throws IOException {
    //mystart = System.currentTimeMillis();   

    WriteString(output, "GIF87a");
   
    WriteScreenDescriptor(output, (short)full_width_, (short)full_height_, numColors_);

    output.write(colors_, 0, colors_.length);

    WriteImageDescriptor(output,(short)full_width_, (short)full_height_);

    byte codesize = BitsNeeded(numColors_);
    if (codesize == 1)
      ++codesize;
    output.write(codesize);

    //System.err.println("took " + (System.currentTimeMillis()- mystart) + " to write header");
    //mystart = System.currentTimeMillis();   

    GIF_LZWStringTable.LZWCompress(output, codesize, pixels_);

    //System.err.println("took " + (System.currentTimeMillis()- mystart) + " to LZW");

    output.write(0);

    WriteTrailer(output);
    output.flush();
  }

  public void WriteScreenDescriptor(OutputStream output, short width, short height, int numColors) throws IOException {
    byte byte_ = 0;

    byte_ |= (((byte)(BitsNeeded(numColors) - 1)) & 7);
    byte_ |= ((byte)1 & 1) << 7;
    byte_ |= ((byte)0 & 1) << 3;
    byte_ |= ((byte)7 & 7) << 4;

    WriteWord(output, width);
    WriteWord(output, height);
    output.write(byte_);
    output.write((byte)0);
    output.write((byte)0);
  }

  public void WriteImageDescriptor(OutputStream output, short width, short height) throws IOException {
    output.write((byte)',');
    WriteWord(output, (short)0);
    WriteWord(output, (short)0);
    WriteWord(output, width);
    WriteWord(output, height);      
    output.write((byte)0);
  }

  public void WriteTrailer(OutputStream output) throws IOException {
    output.write((byte)';');
    WriteWord(output, (short)0);
    WriteWord(output, (short)0);
    WriteWord(output, (short)0);
    WriteWord(output, (short)0);      
    output.write((byte)0);
  }

  public static byte BitsNeeded(int n) {
    byte ret = 1;

    if (n-- == 0)
      return 0;

    while ((n >>= 1) != 0)
      ++ret;
   
    return ret;
  }    

  public static void WriteWord(OutputStream output, short w) throws IOException {
    output.write(w & 0xFF);
    output.write((w >> 8) & 0xFF);
  }
    
  public static void WriteString(OutputStream output, String string) throws IOException {
    for (int loop = 0; loop < string.length(); ++loop)
      output.write((byte)(string.charAt(loop)));
  }
}

class GIF_LZWStringTable
{
  private final static int RES_CODES = 2;
  private final static short HASH_FREE = (short)0xFFFF;
  private final static short NEXT_FIRST = (short)0xFFFF;
  private final static int MAXBITS = 12;
  private final static int MAXSTR = (1 << MAXBITS);
  private final static short HASHSIZE = 9973;
  private final static short HASHSTEP = 2039;

  OutputStream output_ = null;
  byte[] buffer_ = null;
  int index_;
  int bitsLeft_;

  byte strChr_[];
  short strNxt_[];
  short strHsh_[];
  short numStrings_;

  public GIF_LZWStringTable(OutputStream output) {
    strChr_ = new byte[MAXSTR];
    strNxt_ = new short[MAXSTR];
    strHsh_ = new short[HASHSIZE];    

    output_ = output;
    buffer_ = new byte[256];
    index_ = 0;
    bitsLeft_ = 8;
  }

  public int AddCharString(short index, byte b) {
    int hshidx;

    if (numStrings_ >= MAXSTR)
      return 0xFFFF;
   
    hshidx = Hash(index, b);
    while (strHsh_[hshidx] != HASH_FREE)
      hshidx = (hshidx + HASHSTEP) % HASHSIZE;
   
    strHsh_[hshidx] = numStrings_;
    strChr_[numStrings_] = b;
    strNxt_[numStrings_] = (index != HASH_FREE) ? index : NEXT_FIRST;

    return numStrings_++;
  }
    
  public short FindCharString(short index, byte b) {
    int hshidx, nxtidx;

    if (index == HASH_FREE)
      return b;

    hshidx = Hash(index, b);
    while ((nxtidx = strHsh_[hshidx]) != HASH_FREE) {
      if (strNxt_[nxtidx] == index && strChr_[nxtidx] == b)
	return (short)nxtidx;
      hshidx = (hshidx + HASHSTEP) % HASHSIZE;
    }

    return (short)0xFFFF;
  }

  public void ClearTable(int codesize) {
    numStrings_ = 0;
   
    for (int q = 0; q < HASHSIZE; q++) {
      strHsh_[q] = HASH_FREE;
    }

    int w = (1 << codesize) + RES_CODES;
    for (int q = 0; q < w; q++)
      AddCharString((short)0xFFFF, (byte)q);
  }
    
  public static int Hash(short index, byte lastbyte) {
    return ((int)((short)(lastbyte << 8) ^ index) & 0xFFFF) % HASHSIZE;
  }

  public static void LZWCompress(OutputStream output, int codesize, byte toCompress[]) throws IOException {
    byte c;
    short index;
    int clearcode, endofinfo, numbits, limit, errcode;
    short prefix = (short)0xFFFF;

    GIF_LZWStringTable strings = new GIF_LZWStringTable(output);

    clearcode = 1 << codesize;
    endofinfo = clearcode + 1;
    
    numbits = codesize + 1;
    limit = (1 << numbits) - 1;
   
    strings.ClearTable(codesize);
    strings.WriteBits(clearcode, numbits);

    for (int loop = 0; loop < toCompress.length; ++loop) {
      c = toCompress[loop];
      if ((index = strings.FindCharString(prefix, c)) != -1)
	prefix = index;
      else {
	strings.WriteBits(prefix, numbits);
	if (strings.AddCharString(prefix, c) > limit) {
	  if (++numbits > 12) {
	    strings.WriteBits(clearcode, numbits - 1);
	    strings.ClearTable(codesize);
	    numbits = codesize + 1;
	  }
	  limit = (1 << numbits) - 1;
	}
	prefix = (short)((short)c & 0xFF);
      }
    }
   
    if (prefix != -1)
      strings.WriteBits(prefix, numbits);
   
    strings.WriteBits(endofinfo, numbits);
    strings.Flush();

    strings = null;
  }

  public void Flush() throws IOException {
    int numBytes = index_ + (bitsLeft_ == 8 ? 0 : 1);
    if (numBytes > 0) {
      output_.write(numBytes);
      output_.write(buffer_, 0, numBytes);
      buffer_[0] = 0;
      index_ = 0;
      bitsLeft_ = 8;
    }
  }

  public void WriteBits(int bits, int numbits) throws IOException {
    int bitsWritten = 0;
    int numBytes = 255;
    do {
      if ((index_ == 254 && bitsLeft_ == 0) || index_ > 254) {
	output_.write(numBytes);
	output_.write(buffer_, 0, numBytes);

	buffer_[0] = 0;
	index_ = 0;
	bitsLeft_ = 8;
      }

      if (numbits <= bitsLeft_) {
	buffer_[index_] |= (bits & ((1 << numbits) - 1)) << (8 - bitsLeft_);
	bitsWritten += numbits;
	bitsLeft_ -= numbits;
	numbits = 0;
      } else {
	buffer_[index_] |= (bits & ((1 << bitsLeft_) - 1)) << (8 - bitsLeft_);
	bitsWritten += bitsLeft_;
	bits >>= bitsLeft_;
	numbits -= bitsLeft_;
	buffer_[++index_] = 0;
	bitsLeft_ = 8;
      }
    } while (numbits != 0);
  }
}

