 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */
	.text
	.align	1
_string.7:
	.byte	98
	.byte	61
	.byte	37
	.byte	100
	.byte	32
	.byte	99
	.byte	61
	.byte	37
	.byte	100
	.byte	32
	.byte	10
	.byte	0

	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	movl	$0,%eax
	movl	_x+8,%eax
	movl	_aa+12,%eax
	pushl	_aa+12
	pushl	_x+8
	pushl	$_string.7
	call	_printf
	leal	12(%esp),%esp
	movl	$0,%eax
	leave
	ret

	.data
	.align	4
	.global	_x
_x:
	.long	11
	.long	12
	.long	13
	.align	4
	.global	_a
_a:
	.long	0
	.long	1
	.long	2
	.long	3
	.long	4
	.long	5
	.long	6
	.long	7
	.long	8
	.long	9
	.long	10
	.long	11
	.align	4
	.global	_aa
_aa:
	.long	0
	.long	1
	.long	2
	.long	3
	.long	4
	.long	5
	.long	6
	.long	7
	.long	8
	.long	9
	.long	10
	.long	11
