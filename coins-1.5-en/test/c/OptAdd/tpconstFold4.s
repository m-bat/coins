 .ident "Coins Compiler version: coins-1.4.4.3 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */

	.text
	.align	4
	.global	_ff
_ff:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%eax
	cmpl	$0,%eax
	jle	.L5
.L4:
	jmp	.L6
.L5:
	negl	%eax
.L6:
	leave
	ret

	.align	1
_string.18:
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
	.byte	10
	.byte	0

	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$44,%esp
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	movl	$1,%eax
	movl	$4,%eax
	movl	$10,%eax
	movl	$16,%eax
	movl	$1,-36(%ebp)
	movl	-36(%ebp),%eax
	leal	3(%eax),%edi
	pushl	$1
	call	_ff
	leal	4(%esp),%esp
	leal	2(%eax),%eax
	movl	%eax,-44(%ebp)
	movl	-44(%ebp),%eax
	leal	6(%eax),%esi
	leal	12(%esi),%ebx
	pushl	$1
	call	_ff
	leal	4(%esp),%esp
	leal	3(%eax),%ecx
	movl	%ecx,%eax
	imull	$6,%eax
	leal	12(%eax),%edx
	pushl	%edx
	pushl	%eax
	pushl	%ecx
	pushl	%edi
	pushl	%ebx
	pushl	%esi
	pushl	-44(%ebp)
	pushl	$16
	pushl	$10
	pushl	$4
	pushl	$_string.18
	call	_printf
	leal	44(%esp),%esp
	movl	$0,%eax
	popl	%edi
	popl	%esi
	popl	%ebx
	leave
	ret

