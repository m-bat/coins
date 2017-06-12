 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */

	.text
	.align	4
	.global	_f
_f:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%eax
	leal	4(%eax),%eax
	leave
	ret


	.align	4
	.global	_g
_g:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%eax
	pushl	$3
	call	*%eax
	leal	4(%esp),%esp
	leave
	ret

	.align	1
_string.9:
	.byte	37
	.byte	100
	.byte	10
	.byte	0

	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	movl	$_f,%eax
	pushl	$3
	call	*%eax
	leal	4(%esp),%esp
	pushl	%eax
	pushl	$_string.9
	call	_printf
	leal	8(%esp),%esp
	pushl	$_f
	call	_g
	leal	4(%esp),%esp
	pushl	%eax
	pushl	$_string.9
	call	_printf
	leal	8(%esp),%esp
	movl	$0,%eax
	leave
	ret

