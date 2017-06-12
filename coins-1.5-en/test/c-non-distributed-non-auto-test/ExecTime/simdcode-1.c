/* Code fragments of png picture processing program 
   libpng/png_write_find_filter().
   Picked up by Ogawa and modified by Fukuda (Mugi)
 */
typedef unsigned char png_byte;

#define lmins  1023

#define row_bytes 128
void pattern1();
void pattern2();
void pattern3();
void pattern4();

int main()
{
  pattern1();
  pattern2();
  pattern3();
  pattern4();
  return 0;
}
void pattern1()
{
  int i, rpi;
  int sum;
  unsigned char v;
  unsigned char row_buf[1000];

  for ( i = 0, rpi = 1 ; i < row_bytes; i++, rpi++ ) {
    v= row_buf[rpi];
    // v = *rp;
    sum += (v <128) ? v: 256 - v;

  }
}

void pattern2()
{
  int i;
  int sum;
  png_byte v;
  png_byte row_buf[1000];
  png_byte dp[1000], rp[1000];
  int dpi, rpi, lpi;
  
  for ( i = 0, dpi = 0, rpi = 0, lpi = 1;
	i < row_bytes; 
	i++ ) {
    // v = *dp++ = (png_byte)(((int)*rp++ - (int)*lp++) & 0xff);
    v = dp[dpi] = (png_byte)(((int)rp[rpi] - (int)row_buf[lpi]) & 0xff);
    dpi++; rpi++; lpi++;
    sum += (v <128) ? v: 256 - v;
    //if ( sum > lmins) { break;}
  }

}

void pattern3()
{
  int i;
  int sum ;
  unsigned char v;

  png_byte dp[1000], row_buf[1000], prev_row[1000];
  int dpi, rpi, ppi;

  for ( i = 0, rpi = 1, dpi = 0, ppi = 1;
	i < row_bytes; 
	i++ ) {
    // v = *dp++ = (png_byte)(((int)*rp++ - (int)*pp++) & 0xff);
    v = dp[dpi] = (png_byte)(((int)row_buf[rpi] - (int)prev_row[ppi]) & 0xff);
    dpi++; rpi++; ppi++;
    sum += (v <128) ? v: 256 - v;
    //if ( sum > lmins) { break;}
  }

}

void pattern4()
{
  int i;
  int sum ;
  unsigned char v;

  png_byte dp[1000], row_buf[1000], prev_row[1000];
  int dpi, rpi, ppi, lpi;
  int bpp = 24;

  for ( i = 0, rpi = 1, dpi = 0, ppi = 1;
	i < bpp; 
	i++ ) {
    v = dp[dpi] = (png_byte)(((int)row_buf[rpi] - (int)prev_row[ppi]/2) & 0xff);
    dpi++; rpi++; ppi++;
    sum += (v <128) ? v: 256 - v;
    //if ( sum > lmins) { break;}
  }

  for ( lpi = 1;
	i < row_bytes; 
	i++ ) {

    v = dp[dpi] = (png_byte)(((int)row_buf[rpi] - 
			      ((int)prev_row[ppi] + (int)row_buf[lpi]/2))
			     & 0xff);
    dpi++; rpi++; ppi++; lpi++;
    sum += (v <128) ? v: 256 - v;
    //if ( sum > lmins) { break;}
  }

}
