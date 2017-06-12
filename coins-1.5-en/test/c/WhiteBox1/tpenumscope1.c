/* tpenumscope1.c (Scope) local variable have the same name as enum. */
/*                 from crafty/movegen.c */
  typedef enum {empty=0, pawn=1, knight=2, king=3, 
                bishop=5, rook=6, queen=7} PIECE;
int GenerateCheckEvasions(int ply, int wtm, int *move)
{
  register int padvances1_all, empty = 20, checksqs;
  *move = empty;
  empty=10; /** This may cause "invalid lvalue in assignment".  **/
  return empty;
}
int main()
{
  int empty, x;
  PIECE piece1 = king;
  empty = GenerateCheckEvasions(1, 8, &x);
  printf("%d %d %d \n", empty, x, piece1);
  return 0;

}
