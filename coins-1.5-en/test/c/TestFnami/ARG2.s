 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */
	.text
	.align	1
_string.6:
	.byte	97
	.byte	114
	.byte	103
	.byte	118
	.byte	91
	.byte	37
	.byte	100
	.byte	93
	.byte	61
	.byte	34
	.byte	37
	.byte	115
	.byte	34
	.byte	10
	.byte	0

	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	movl	8(%ebp),%ecx
	movl	12(%ebp),%eax
	leal	-1(%ecx),%edi
	leal	4(%eax),%esi
	movl	$0,%ebx
.L3:
	cmpl	%edi,%ebx
	jge	.L5
.L4:
	pushl	(%esi,%ebx,4)
	pushl	%ebx
	pushl	$_string.6
	call	_printf
	leal	12(%esp),%esp
	leal	1(%ebx),%ebx
	jmp	.L3
.L5:
	movl	$0,%eax
	popl	%edi
	popl	%esi
	popl	%ebx
	leave
	ret

