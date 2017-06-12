/* tpswitchConstExp1.c: Spec/crafty/validate.c */

 typedef enum {empty=0, pawn=1, knight=2, king=3, 
                bishop=5, rook=6, queen=7} PIECE;
main()
{
  PIECE x;
  int a, b, c;
  a = 0;
  x = king;
  switch(x) {
  case -king: a = 1; break;
  case  king: a = 2; break;
  default:    a = 3;
         }
  printf("%d %d \n", a, queen);
  return 0;
}

