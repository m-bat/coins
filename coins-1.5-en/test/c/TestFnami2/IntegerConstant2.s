 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */
	.text
	.align	1
_string.3:
	.byte	37
	.byte	48
	.byte	56
	.byte	88
	.byte	32
	.byte	37
	.byte	48
	.byte	56
	.byte	88
	.byte	32
	.byte	37
	.byte	48
	.byte	56
	.byte	88
	.byte	10
	.byte	0
	.align	1
_string.5:
	.byte	37
	.byte	48
	.byte	56
	.byte	88
	.byte	32
	.byte	37
	.byte	48
	.byte	56
	.byte	88
	.byte	32
	.byte	37
	.byte	48
	.byte	56
	.byte	88
	.byte	32
	.byte	37
	.byte	48
	.byte	56
	.byte	88
	.byte	32
	.byte	37
	.byte	48
	.byte	56
	.byte	88
	.byte	10
	.byte	0
	.align	1
_string.7:
	.byte	37
	.byte	48
	.byte	56
	.byte	108
	.byte	88
	.byte	32
	.byte	37
	.byte	48
	.byte	56
	.byte	108
	.byte	88
	.byte	32
	.byte	37
	.byte	48
	.byte	56
	.byte	108
	.byte	88
	.byte	10
	.byte	0
	.align	1
_string.9:
	.byte	37
	.byte	48
	.byte	56
	.byte	108
	.byte	88
	.byte	32
	.byte	37
	.byte	48
	.byte	56
	.byte	108
	.byte	88
	.byte	32
	.byte	37
	.byte	48
	.byte	56
	.byte	108
	.byte	88
	.byte	32
	.byte	37
	.byte	48
	.byte	56
	.byte	108
	.byte	88
	.byte	32
	.byte	37
	.byte	48
	.byte	56
	.byte	108
	.byte	88
	.byte	10
	.byte	0
	.align	1
_string.11:
	.byte	37
	.byte	103
	.byte	32
	.byte	37
	.byte	103
	.byte	32
	.byte	37
	.byte	103
	.byte	32
	.byte	37
	.byte	103
	.byte	32
	.byte	37
	.byte	103
	.byte	32
	.byte	37
	.byte	103
	.byte	10
	.byte	0

	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	_i2
	pushl	_i1
	pushl	_i0
	pushl	$_string.3
	call	_printf
	leal	16(%esp),%esp
	pushl	_ui4
	pushl	_ui3
	pushl	_ui2
	pushl	_ui1
	pushl	_ui0
	pushl	$_string.5
	call	_printf
	leal	24(%esp),%esp
	pushl	_l2
	pushl	_l1
	pushl	_l0
	pushl	$_string.7
	call	_printf
	leal	16(%esp),%esp
	pushl	_ul4
	pushl	_ul3
	pushl	_ul2
	pushl	_ul1
	pushl	_ul0
	pushl	$_string.9
	call	_printf
	leal	24(%esp),%esp
	pushl	_d5+4
	pushl	_d5
	pushl	_d4+4
	pushl	_d4
	pushl	_d3+4
	pushl	_d3
	pushl	_d2+4
	pushl	_d2
	pushl	_d1+4
	pushl	_d1
	pushl	_d0+4
	pushl	_d0
	pushl	$_string.11
	call	_printf
	leal	52(%esp),%esp
	movl	$0,%eax
	leave
	ret

	.data
	.align	4
	.global	_i0
_i0:
	.long	-1
	.align	4
	.global	_i1
_i1:
	.long	-1
	.align	4
	.global	_i2
_i2:
	.long	-1
	.align	4
	.global	_ui0
_ui0:
	.long	134217727
	.align	4
	.global	_ui1
_ui1:
	.long	134217727
	.align	4
	.global	_ui2
_ui2:
	.long	67108864
	.align	4
	.global	_ui3
_ui3:
	.long	134217727
	.align	4
	.global	_ui4
_ui4:
	.long	75497472
	.align	4
	.global	_l0
_l0:
	.long	-1
	.align	4
	.global	_l1
_l1:
	.long	-1
	.align	4
	.global	_l2
_l2:
	.long	-1
	.align	4
	.global	_ul0
_ul0:
	.long	134217727
	.align	4
	.global	_ul1
_ul1:
	.long	134217727
	.align	4
	.global	_ul2
_ul2:
	.long	67108864
	.align	4
	.global	_ul3
_ul3:
	.long	134217727
	.align	4
	.global	_ul4
_ul4:
	.long	75497472
	.align	4
	.global	_d0
_d0:
	.long	0xff800000,0x41efffff /* 4.294967292E9 */
	.align	4
	.global	_d1
_d1:
	.long	0x0,0x41e80000 /* 3.221225472E9 */
	.align	4
	.global	_d2
_d2:
	.long	0x0,0x41e40000 /* 2.68435456E9 */
	.align	4
	.global	_d3
_d3:
	.long	0xff800000,0x41efffff /* 4.294967292E9 */
	.align	4
	.global	_d4
_d4:
	.long	0x0,0x41e80000 /* 3.221225472E9 */
	.align	4
	.global	_d5
_d5:
	.long	0x0,0x41e40000 /* 2.68435456E9 */
