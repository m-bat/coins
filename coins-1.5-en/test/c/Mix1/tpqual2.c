/* tpqual2.cc -- Qualifier selection (C struct) */

void *calloc( long pCount, long pSize ); /* 051016 */

struct RegInf;
                          
struct SymbolT {         
  int   kind;         
  union {                   
    struct RegInf*  reg1;        
    struct RegInf*  reg2;        
  } a1;
};   
/* RegInf* RegP(SymbolP p) { return p->a1.reg; } */
 
typedef struct SymbolT* SymbolP;       

struct RegInf {
  SymbolP      boundVar;     
  int          reusable;     
};              
 
void RegAlloc(SymbolP pReg)
{
  SymbolP lRegSym;
  
  lRegSym = pReg;
  if ((lRegSym->kind==0?lRegSym->a1.reg1 :lRegSym->a1.reg2)
       ->boundVar == 0) {
    lRegSym->kind==0;
    lRegSym->a1.reg2->reusable = 0;
  }
}  

int main()
{
  SymbolP lSym = (SymbolP)(calloc(1, sizeof(struct SymbolT)));
  struct RegInf *lReg = (struct RegInf*)(calloc(1, sizeof(struct RegInf)));
  lReg->boundVar = lSym;
  lSym->a1.reg1 = lReg;
  lSym->a1.reg2 = lReg;
  RegAlloc(lSym);
  printf("%d %d \n", lSym->kind, lReg->reusable);
}

