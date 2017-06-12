 .ident "Coins Compiler version: coins-1.4.4.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */
	.text
	.align	1
_string.12:
	.byte	97
	.byte	61
	.byte	37
	.byte	100
	.byte	32
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
	.byte	105
	.byte	61
	.byte	37
	.byte	100
	.byte	32
	.byte	107
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
	subl	$12,%esp
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	movl	$1,%edx
	movl	$2,%ecx
	movl	$0,-4(%ebp)
.L3:
	movl	-4(%ebp),%eax
	cmpl	$8,%eax
	jge	.L15
.L4:
	movl	$0,%edi
.L5:
	cmpl	$8,%edi
	jge	.L14
.L6:
	movl	$0,%esi
.L7:
	cmpl	$5,%esi
	jge	.L13
.L8:
	movl	$0,%eax
.L9:
	cmpl	$5,%eax
	jge	.L12
.L10:
	leal	(%edx,%ecx),%ebx
	leal	1(%eax),%eax
	jmp	.L9
.L12:
	leal	1(%esi),%esi
	jmp	.L7
.L13:
	leal	1(%edi),%edi
	jmp	.L5
.L14:
	movl	-4(%ebp),%eax
	leal	1(%eax),%eax
	movl	%eax,-4(%ebp)
	jmp	.L3
.L15:
	pushl	-8(%ebp)
	pushl	-12(%ebp)
	pushl	%ebx
	pushl	%ecx
	pushl	%edx
	pushl	$_string.12
	call	_printf
	leal	24(%esp),%esp
.L16:
	popl	%edi
	popl	%esi
	popl	%ebx
	leave
	ret

