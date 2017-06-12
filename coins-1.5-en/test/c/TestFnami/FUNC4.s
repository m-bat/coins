 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */
	.text
	.align	1
_string.12:
	.byte	37
	.byte	100
	.byte	32
	.byte	37
	.byte	100
	.byte	32
	.byte	37
	.byte	100
	.byte	32
	.byte	37
	.byte	100
	.byte	32
	.byte	37
	.byte	100
	.byte	32
	.byte	32
	.byte	37
	.byte	100
	.byte	32
	.byte	37
	.byte	100
	.byte	32
	.byte	37
	.byte	100
	.byte	32
	.byte	37
	.byte	100
	.byte	32
	.byte	37
	.byte	100
	.byte	10
	.byte	0

	.align	4
	.global	_f
_f:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$16,%esp
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	movl	8(%ebp),%eax
	movl	%eax,-16(%ebp)
	movl	12(%ebp),%eax
	movl	%eax,-12(%ebp)
	movl	16(%ebp),%eax
	movl	%eax,-8(%ebp)
	movl	20(%ebp),%eax
	movl	%eax,-4(%ebp)
	movl	24(%ebp),%edi
	movl	28(%ebp),%esi
	movl	32(%ebp),%ebx
	movl	36(%ebp),%edx
	movl	40(%ebp),%ecx
	movl	44(%ebp),%eax
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	pushl	-4(%ebp)
	pushl	-8(%ebp)
	pushl	-12(%ebp)
	pushl	-16(%ebp)
	pushl	$_string.12
	call	_printf
	leal	44(%esp),%esp
.L3:
	popl	%edi
	popl	%esi
	popl	%ebx
	leave
	ret


	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	$10
	pushl	$9
	pushl	$8
	pushl	$7
	pushl	$6
	pushl	$5
	pushl	$4
	pushl	$3
	pushl	$2
	pushl	$1
	call	_f
	leal	40(%esp),%esp
	movl	$0,%eax
	leave
	ret

