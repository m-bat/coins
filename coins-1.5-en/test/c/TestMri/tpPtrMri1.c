/* tpPtrMri1.c  K & R A8.6.1 */

int printf(char*, ...);

int i, *pi, *const cpi = &i;
const int ci = 3, *pci;
int j;

int
main()
{
int i2, *pi2, *const cpi2 = &i2;
const int ci2 = 3, *pci2;

*cpi = 1;
pci = &ci;

printf("*cpi %d *pci %d \n", *cpi, *pci);

}

