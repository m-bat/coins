/* tpenumscope1.c (Scope) enum and local variable are the same name */
/*                 from crafty/movegen.c */
  typedef enum {empty=0, pawn=1, knight=2, king=3,
                bishop=5, rook=6, queen=7} PIECE;
int GenerateCheckEvasions(int ply, int wtm, int *move)
{
  register int padvances1_all, empty, checksqs;
  int x;
  x = empty;
/**
  empty=10;
    causes "invalid lvalue in assignment".
**/
  empty = 10; /* SF030609 */
  return 0;
}

main() /* SF030609 */
{
  printf("Name confliction test of enum and local variable"); /* SF030609 */
}
