 .ident "Coins Compiler version: coins-1.4.4.3 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */

	.text
	.align	4
	.global	_f
_f:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$4,%esp
	movl	12(%ebp),%eax
	movl	%eax,-4(%ebp)
	leal	-4(%ebp),%eax
	movl	8(%ebp),%ecx
	movl	(%eax),%eax
	movl	%eax,(%ecx)
.L3:
	leave
	ret

	.align	1
_string.11:
	.byte	32
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
	subl	$16,%esp
	pushl	$1
	leal	-16(%ebp),%eax
	pushl	%eax
	call	_f
	leal	8(%esp),%esp
	movl	-16(%ebp),%eax
	movl	%eax,-8(%ebp)
	leal	-8(%ebp),%eax
	leal	-12(%ebp),%ecx
	movl	(%eax),%eax
	movl	%eax,(%ecx)
	leal	-12(%ebp),%eax
	leal	-4(%ebp),%ecx
	movl	(%eax),%eax
	movl	%eax,(%ecx)
	pushl	-4(%ebp)
	pushl	$_string.11
	call	_printf
	leal	8(%esp),%esp
	movl	$0,%eax
	leave
	ret

