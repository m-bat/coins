/* tpstructPtrNakata2.c:  struct and pointer */
/*   Nakata mail 040213 */
/* Display representation in nested function for PL0 */

int printf(char*, ...);

static void *display_0;
static void *display_1;

main(){
  void *saveDisplay;
  struct main_frame{
    int x; int y;
  }frame_;
  struct sub_frame{
    int a; int b;
  }frame_1;
  frame_.y = 10;
  saveDisplay = display_0;
  display_0 = &frame_;
  ((struct main_frame*)display_0)->x = ((struct main_frame*)display_0)->y;
  display_1 = &frame_1;
  ((struct sub_frame*)display_1)->a = ((struct main_frame*)display_0)->x;
  printf(" %d \n",  ((struct sub_frame*)display_1)->a);
  display_0 = saveDisplay;
  return 0;
}

