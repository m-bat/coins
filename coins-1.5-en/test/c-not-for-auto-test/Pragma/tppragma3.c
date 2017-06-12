/* tppragma3.c (Kitamura mail 060808) */

int ia[4];

int bar()
{
#pragma key_only2
    int ib[4];
    ia[0] += ib[0];
    ia[1] += ib[1];
#pragma key_only3
    ia[2] += ib[2];
#pragma dummy ia
#pragma dummy ib
    ia[3] += ib[3];
#pragma loop dummy_label

 dummy_label:
#pragma optimize size
#pragma optimize speed
 dummy_label2:
#pragma loop dummy_label2
 dummy_label3:
   ia[3] = ia[3] + 1;
   return 0;
}
