 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */
	.text
	.align	1
_string.4:
	.byte	37
	.byte	117
	.byte	10
	.byte	0

	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	movl	$0,%eax
	pushl	$0
	pushl	$_string.4
	call	_printf
	leal	8(%esp),%esp
	movl	$0,%eax
	leave
	ret

