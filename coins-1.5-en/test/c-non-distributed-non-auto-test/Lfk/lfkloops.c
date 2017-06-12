/* lfkloops: Digest of Livermore loop KERNEL programs */

#pragma doAll subpParallel KERNEL
#ifndef MAIN
#define MAIN
#endif
#include "emcoins_thread_framework.h"

/* Declarations and routines for data-initiation/reporting */

/* Main program common to all kernel loops */
int main()
{
  int    i, j, k;    /**/

  double ti, tj, tock; /**/

#pragma emcoins_thread init
  /* iou = stdio; */
  cumtim[0] = 0.0;
  ti = SECOND(cumtim[0]);
  
  Komput = "MicroBlaze              ";
  Kontrl = "EDK                     ";
  Kompil = "COINS-emb 1.3.3         ";
  Kalend = "2006.08.31              ";
  Identy = "Tetsuro Fujise, MRI     ";

  INDATA(TK);
  TRACE("main   ");
  VERIFY( );
  tj = SECOND(cumtim[0]);
  SIZES(-1);

  printf("\n Mruns=%d \n", Mruns); /*****/
  for (i = 0; i < 24; i++) {
    TESTS(i, TEMPUS);
  }
  for (k = 0; k < Mruns; k++) {
    printf("\n im=%d ml=%d\n", im, ml); /*****/
    i = k;
    jr = (i-1) % 7 + 1;
    for (j = im; j <= ml; j++) {
      il = j;
      KERNEL(TK);
    }
  }
#pragma emcoins_thread end
 
  return 0;
} /* main */

/* ... */

/* KERNEL part of lfkloop-1.c */

void KERNEL( double TK[6] )
{
  int II, IPNTP, IPNT, NG, NZ, LB, KN, JN, KB5I;
  int i, j, k, mpysav, it0, lw, nl1, nl2, kx, ky, i1, i2, j1, j2, j4,
      ink ;
  double WH, dw, tw, di, dn, fw;
  double temp, temp1, temp2, temp3;

  TRACE("KERNEL  ");

  mpy   = 1;
  mpysav= mpylim;
  Loop2= 1;
  mpylim= Loop2;
  L     = 1;
  Loop  = 2;  /*##*/
  n     = 64;   /*##*/
  LP    = Loop;

  loopCount++;
  do {
#pragma doAll parallel
    for (k = 0; k < n; k++) {
      X[k]= Q + Y[k] * (R * ZX[k+10] + T * ZX[k+11]);
    }
  } while (TEST(1) > 0);

} /* KERNEL-1 */


/* KERNEL part of lfkloop-3.c */

void KERNEL( double TK[6] )
{
  int II, IPNTP, IPNT, NG, NZ, LB, KN, JN, KB5I;
  int i, j, k, mpysav, it0, lw, nl1, nl2, kx, ky, i1, i2, j1, j2, j4,
      ink ;
  double WH, dw, tw, di, dn, fw;
  double temp, temp1, temp2, temp3;

  TRACE("KERNEL  ");

  mpy   = 1;
  mpysav= mpylim;
  Loop2= 1;
  mpylim= Loop2;
  L     = 1;
  Loop  = 1;  /*##*/
  n     = 64;   /*##*/
  LP    = Loop;
  Loop2= mpysav;
  mpylim= Loop2;

  printf("\nKERNEL n=%d nn=%d Loop=%d Loop2=%d\n", n, nn, Loop, Loop2); /*****/

  do {
    Q= 0.000;
#pragma doAll parallel
    for (k = 0; k < n; k++) {
      Q= Q + Z[k] * X[k];
    }
  } while (TEST(3) > 0);

} /* KERNEL-3 */

/* KERNEL part of lfkloop-7.c */

void KERNEL( double TK[6] )
{
  int II, IPNTP, IPNT, NG, NZ, LB, KN, JN, KB5I;
  int i, j, k, mpysav, it0, lw, nl1, nl2, kx, ky, i1, i2, j1, j2, j4,
      ink ;
  double WH, dw, tw, di, dn, fw;
  double temp, temp1, temp2, temp3;

  TRACE("KERNEL  ");

  mpy   = 1;
  mpysav= mpylim;
  Loop2= 1;
  mpylim= Loop2;
  L     = 1;
  Loop  = 1;  /*##*/
  n     = 64;   /*##*/
  LP    = Loop;
  Loop2= mpysav;
  mpylim= Loop2;

  printf("\nKERNEL n=%d nn=%d Loop=%d Loop2=%d\n", n, nn, Loop, Loop2); /*****/

  do {
#pragma doAll parallel
    for (k = 0; k < n; k++) {
      X[k]=     U[k  ] + R*( Z[k  ] + R*Y[k  ]) +
             T*( U[k+3] + R*( U[k+2] + R*U[k+1]) +
             T*( U[k+6] + Q*( U[k+5] + Q*U[k+4])));
    }
  } while (TEST(7) > 0);

} /* KERNEL-7 */

/* KERNEL part of lfkloop-9.c */

void KERNEL( double TK[6] )
{
  int II, IPNTP, IPNT, NG, NZ, LB, KN, JN, KB5I;
  int i, j, k, mpysav, it0, lw, nl1, nl2, kx, ky, i1, i2, j1, j2, j4,
      ink ;
  double WH, dw, tw, di, dn, fw;
  double temp, temp1, temp2, temp3;

  TRACE("KERNEL  ");

  mpy   = 1;
  mpysav= mpylim;
  Loop2= 1;
  mpylim= Loop2;
  L     = 1;
  Loop  = 1;  /*##*/
  n     = 64;   /*##*/
  LP    = Loop;
  Loop2= mpysav;
  mpylim= Loop2;

  printf("\nKERNEL n=%d nn=%d Loop=%d Loop2=%d\n", n, nn, Loop, Loop2); /*****/

  do {
#pragma doAll parallel
    for (k = 0; k < n; k++) {
      PX[k][0]= DM28*PX[k][12] + DM27*PX[k][11] + DM26*PX[k][10] +
                DM25*PX[k][ 9] + DM24*PX[k][ 8] + DM23*PX[k][ 7] +
                DM22*PX[k][ 6] +  C0*(PX[k][ 4] +      PX[k][ 5])+ PX[k][ 2];
    }
  } while (TEST(9) > 0);

} /* KERNEL-9 */


/* KERNEL part of lfkloop-10.c */

void KERNEL( double TK[6] )
{
  int II, IPNTP, IPNT, NG, NZ, LB, KN, JN, KB5I;
  int i, j, k, mpysav, it0, lw, nl1, nl2, kx, ky, i1, i2, j1, j2, j4,
      ink ;
  double WH, dw, tw, di, dn, fw;
  double temp, temp1, temp2, temp3;

  TRACE("KERNEL  ");

  mpy   = 1;
  mpysav= mpylim;
  Loop2= 1;
  mpylim= Loop2;
  L     = 1;
  Loop  = 1;  /*##*/
  n     = 64;   /*##*/
  LP    = Loop;
  Loop2= mpysav;
  mpylim= Loop2;

  printf("\nKERNEL n=%d nn=%d Loop=%d Loop2=%d\n", n, nn, Loop, Loop2); /*****/

  do {
#pragma doAll parallel
    for (k = 0; k < n; k++) {
      AR       =      CX[k][ 4];
      BR       = AR - PX[k][ 4];
      PX[k][ 4]= AR;
      CR       = BR - PX[k][ 5];
      PX[k][ 5]= BR;
      AR       = CR - PX[k][ 6];
      PX[k][ 6]= CR;
      BR       = AR - PX[k][ 7];
      PX[k][ 7]= AR;
      CR       = BR - PX[k][ 8];
      PX[k][ 8]= BR;
      AR       = CR - PX[k][ 9];
      PX[k][ 9]= CR;
      BR       = AR - PX[k][10];
      PX[k][10]= AR;
      CR       = BR - PX[k][11];
      PX[k][11]= BR;
      PX[k][13]= CR - PX[k][12];
      PX[k][12]= CR;
    }
  } while (TEST(10) > 0);

} /* KERNEL-10 */

/* KERNEL part of lfkloop-12.c */

void KERNEL( double TK[6] )
{
  int II, IPNTP, IPNT, NG, NZ, LB, KN, JN, KB5I;
  int i, j, k, mpysav, it0, lw, nl1, nl2, kx, ky, i1, i2, j1, j2, j4,
      ink ;
  double WH, dw, tw, di, dn, fw;
  double temp, temp1, temp2, temp3;

  TRACE("KERNEL  ");

  mpy   = 1;
  mpysav= mpylim;
  Loop2= 1;
  mpylim= Loop2;
  L     = 1;
  Loop  = 1;  /*##*/
  n     = 64;   /*##*/
  LP    = Loop;
  Loop2= mpysav;
  mpylim= Loop2;

  printf("\nKERNEL n=%d nn=%d Loop=%d Loop2=%d\n", n, nn, Loop, Loop2); /*****/

  do {
#pragma doAll parallel
    for (k = 0; k < n; k++) {
      X[k] = Y[k+1] - Y[k];
    }
  } while (TEST(12) > 0);

} /* KERNEL-12 */

/* KERNEL part of lfkloop-13.c */
void KERNEL( double TK[6] )
{
  int II, IPNTP, IPNT, NG, NZ, LB, KN, JN, KB5I;
  int i, j, k, mpysav, it0, lw, nl1, nl2, kx, ky, i1, i2, j1, j2, j4,
      ink ;
  double WH, dw, tw, di, dn, fw;
  double temp, temp1, temp2, temp3;

  TRACE("KERNEL  ");

  mpy   = 1;
  mpysav= mpylim;
  Loop2= 1;
  mpylim= Loop2;
  L     = 1;
  Loop  = 1;  /*##*/
  n     = 64;   /*##*/
  LP    = Loop;
  Loop2= mpysav;
  mpylim= Loop2;

  printf("\nKERNEL n=%d nn=%d Loop=%d Loop2=%d\n", n, nn, Loop, Loop2); /*****/

  do {
    fw= 1.000;
#pragma doAll parallel
    for (k = 0; k < n; k++) {
                i1= P[k][0];
                j1= P[k][1];
                i1= i1 & 63;  /*##*/
                j1= j1 & 63;  /*##*/
           P[k][2]= P[k][2]  + B[j1][i1];
           P[k][3]= P[k][3]  + C[j1][i1];
           P[k][0]= P[k][0]  + P[k][2];
           P[k][1]= P[k][1]  + P[k][3];
                i2= P[k][0];
                j2= P[k][1];
                i2= i2 & 63; /*##*/
                j2= j2 & 63; /*##*/
           P[k][0]= P[k][0]  + Y[i2+32];
           P[k][1]= P[k][1]  + Z[j2+32];
                i2= i2       + E[i2+32];
                j2= j2       + F[j2+32];
         H[j2][i2]= H[j2][i2] + fw;
    }
  } while (TEST(13) > 0);

} /* KERNEL-13 */

/* KERNEL part of lfkloop-14.c */

void KERNEL( double TK[6] )
{
  int II, IPNTP, IPNT, NG, NZ, LB, KN, JN, KB5I;
  int i, j, k, mpysav, it0, lw, nl1, nl2, kx, ky, i1, i2, j1, j2, j4,
      ink ;
  double WH, dw, tw, di, dn, fw;
  double temp, temp1, temp2, temp3;

  TRACE("KERNEL  ");

  mpy   = 1;
  mpysav= mpylim;
  Loop2= 1;
  mpylim= Loop2;
  L     = 1;
  Loop  = 1;  /*##*/
  n     = 64;   /*##*/
  LP    = Loop;
  Loop2= mpysav;
  mpylim= Loop2;

  printf("\nKERNEL n=%d nn=%d Loop=%d Loop2=%d\n", n, nn, Loop, Loop2); /*****/

  do {
    fw= 1.000;
#pragma doAll parallel
    for (k = 0; k < n; k++) {
            VX[k]= 0.0;
            XX[k]= 0.0;
            IX[k]= (int)(  GRD[k]);
            XI[k]= (double)( IX[k]);
           EX1[k]= EX   [ IX[k]];
          DEX1[k]= DEX  [ IX[k]];
    }
    for (k = 1; k < n; k++) {
            VX[k]= VX[k] + EX1[k] + (XX[k] - XI[k])*DEX1[k];
            XX[k]= XX[k] + VX[k]  + FLX;
            IR[k]= XX[k];
            RX[k]= XX[k] - IR[k];
            IR[k]= (IR[k] & 2047); /*##*/
            XX[k]= RX[k] + IR[k];
    }
    for (k = 0; k < n; k++) {
      RH[IR[k]  ]= RH[IR[k]  ] + fw - RX[k];
      RH[IR[k]+1]= RH[IR[k]+1] + RX[k];
    }
  } while (TEST(14) > 0);

} /* KERNEL-14 */

