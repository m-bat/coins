/* tpasm4.c: ASM test 4 */ 
/* Kitamura mail 060608 */

int printf(char*, ...);
int puts(char*);

int vec[10] = {1, 2, 3, 4, 5, 6, 7, 8, 10};
int x, y, z;
 
int ex2(int i) 
{
    int autov = 8;
    int x;

    /* x = vec[i] + autov; */
    x = vec[i] + autov;
    asm("#param a,s,%I32,w%I32\n"
        " mov %2(,%3,4),%4\n"
        " add %1(%%ebp),%4\n",
        &autov, vec, i, x);
  return x;
}

void fun(char *, int *, int[], int, int);
void putsptr(char *pString);

int fun2(int i) 
{
    int autov = 9;
    int x;

    /* x = vec[i] + autov; */
    fun("#param a,s,%I32,w%I32\n"
        " mov %2(,%3,4),%4\n"
        " add %1(%%ebp),%4\n",
        &autov, vec, i, x);
    return x;
}

void fun3(char *, char *);
int foo()
{
    fun3("#param s\n"
        "#clobber %eax\n"
        "mov %1, %%eax\n"
        "call puts\n",
        "test0");

    puts("test1"); /**/
    asm("#param s\n"
        "#clobber %eax\n"
        "mov %1, %%eax\n"
        "call puts\n",
        "test1");

    printf("\n &string %x %x %x %s %s\n", 
         &"test2", "test2", *"test2",&"test2", "test2");
    putsptr(&"test2"); /**/
    asm("#param s\n"
        "#clobber %eax\n"
        "mov %1, %%eax\n"
        "call puts\n",
        &"test2");
}

void 
fun(char *pAsmInstructions, int *pPtrParam, int pArrayParam[], 
    int pIntParam1, int pIntParam2)
{
  printf("\nfun %s", pAsmInstructions);
  /* param1 of ASM should be string constant */
  /* asm(pAsmInstructions, pPtrParam, pIntParam1, pIntParam2); */
}
void fun3(char *pAsmInstructions, char *pStringParam)
{
  printf("\nfun3 %s %s ", pAsmInstructions, pStringParam);
  /* asm(pAsmInstructions, pStringParam); */ /* param1 should be string constant */
}

void putsptr(char *pString)
{
  printf("\nputsptr %x %s\n", pString, pString);
}

int main()
{
  int a, b, c;
  a = 1;
  b = a + 2;
  z = -1;
  asm("#param %I32, %I32, w%I32\n"
      "mov %1,%3\n"
      "add %2,%3\n",
      a, b+1, z);
  printf("a=%d b=%d z=%d \n", a, b, z);
  foo();
  y = ex2(b);
  printf("y=%d \n", y);
  return 0;
} 

