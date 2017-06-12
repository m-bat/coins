/* tpStructMri5.c  struct typedef (Fujise 2002/8/6) */

struct RegInf;

struct SymbolT {
  int kind;
  union {
    struct RegInf *reg1;
    struct RegInf *reg2;
  } a1;
};

typedef struct SymbolT* SymbolP;

struct RegInf {
  SymbolP boundVar;
  int     reusable;
};

void RegAlloc(SymbolP pReg)
{
  SymbolP lReg, lAlloc;

  lReg = pReg;

  lAlloc = lReg; /* SF030509 */

  (lAlloc->kind == 5?lAlloc->a1.reg1:lAlloc->a1.reg2)->reusable = 0;
}
main() /* SF030509 */
{
  struct RegInf  reginf = {0,1}; /* SF030509 */
  struct SymbolT symbol = {5,{&reginf}}; /* SF030509 */
  RegAlloc(&symbol); /* SF030509 */
  printf("symbol.kind = %d\n",symbol.kind); /* SF030509 */
  printf("reginf.reusable = %d\n",reginf.reusable); /* SF030509 */
}
