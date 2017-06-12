 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */

	.text
	.align	4
	.global	_f
_f:
	pushl	%ebp
	movl	%esp,%ebp
	movl	$12345,%eax
	leave
	ret

	.align	1
_string.4:
	.byte	37
	.byte	100
	.byte	10
	.byte	0

	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	call	_f
	pushl	%eax
	pushl	$_string.4
	call	_printf
	leal	8(%esp),%esp
	movl	$0,%eax
	leave
	ret

