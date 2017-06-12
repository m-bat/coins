/* tpstringpParam2.c: */ 

int vec[10] = {1, 2, 3, 4, 5, 6, 7, 8, 10};
int x, y, z;
 
void fun(char *, int *, int[], int, int);
void putsptr(char *pString);

int fun2(int i) 
{
    int autov = 9;
    int x;
    x = vec[i] + autov;
    puts("\nfun2 "); 
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
    printf("\n &string %x %x %x %s %s\n", 
         &"test2", "test2", *"test2",&"test2", "test2");
    putsptr(&"test2"); /**/
}

void 
fun(char *pAsmInstructions, int *pPtrParam, int pArrayParam[], 
    int pIntParam1, int pIntParam2)
{
  printf("\nfun %s", pAsmInstructions);

}
void fun3(char *pAsmInstructions, char *pStringParam)
{
  printf("\nfun3 %s", pAsmInstructions);
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
  printf("a=%d b=%d z=%d \n", a, b, z);
  foo();
  y = fun2(b);
  printf("y=%d \n", y);
  return 0;
} 

