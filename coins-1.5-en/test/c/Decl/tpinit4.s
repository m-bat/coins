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
	.byte	10
	.byte	0

	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	movl	$0,%eax
	movl	_x+8,%ecx
	movl	_a+20,%eax
	pushl	%eax
	pushl	%ecx
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
