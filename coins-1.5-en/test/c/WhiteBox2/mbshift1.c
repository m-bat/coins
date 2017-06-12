/* mbshift1.c: Shift operation */ 

int printf(char*, ...);
int main()
{
  int   i1, i2, i3, i4, i5, i6, i7, i8;
  int   j1, j2;
  unsigned int   ui1, ui2, ui3, ui4, ui5, ui6, ui7, ui8;
  j1 = 1;
  j2 = 2;
  i1 = -1;
  i2 = i1 << 3;
  i3 = i1 << j1;
  i4 = 255 >> 4;
  i5 = 255 >> j2;
  printf("i1 %d i2 %d i3 %d i4 %d i5 %d\n",
    i1, i2, i3, i4, i5); 
  ui1 = -1;
  ui2 = ui1 << 3;
  ui3 = ui1 << j1;
  ui4 = 255 >> 4;
  ui5 = 255 >> j2;
  printf("ui1 %d ui2 %d ui3 %d ui4 %d ui5 %d\n",
    ui1, ui2, ui3, ui4, ui5); 
  return 0;
} 

