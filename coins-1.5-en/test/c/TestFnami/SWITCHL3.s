 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */
	.text
	.align	1
_string.4:
	.byte	98
	.byte	97
	.byte	100
	.byte	58
	.byte	32
	.byte	37
	.byte	100
	.byte	37
	.byte	37
	.byte	53
	.byte	33
	.byte	61
	.byte	50
	.byte	10
	.byte	0
	.align	1
_string.6:
	.byte	98
	.byte	97
	.byte	100
	.byte	58
	.byte	32
	.byte	37
	.byte	100
	.byte	37
	.byte	37
	.byte	53
	.byte	61
	.byte	61
	.byte	50
	.byte	10
	.byte	0
	.align	1
_string.8:
	.byte	83
	.byte	119
	.byte	105
	.byte	116
	.byte	99
	.byte	104
	.byte	32
	.byte	116
	.byte	101
	.byte	115
	.byte	116
	.byte	10
	.byte	0

	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	movl	$0,%ebx
.L3:
	cmpl	$15,%ebx
	jge	.L12
.L4:
	cmpl	$2,%ebx
	je	.L5
.L15:
	cmpl	$7,%ebx
	je	.L5
.L16:
	cmpl	$12,%ebx
	jne	.L8
.L5:
	movl	$5,%ecx
	movl	%ebx,%eax
	cdq
	idivl	%ecx
	movl	%edx,%eax
	cmpl	$2,%eax
	je	.L11
.L6:
	pushl	%ebx
	pushl	$_string.4
	call	_printf
	leal	8(%esp),%esp
	jmp	.L11
.L8:
	movl	$5,%ecx
	movl	%ebx,%eax
	cdq
	idivl	%ecx
	cmpl	$2,%edx
	jne	.L11
.L9:
	pushl	%ebx
	pushl	$_string.6
	call	_printf
	leal	8(%esp),%esp
.L11:
	leal	1(%ebx),%ebx
	jmp	.L3
.L12:
	pushl	$_string.8
	call	_printf
	leal	4(%esp),%esp
	movl	$0,%eax
	popl	%ebx
	leave
	ret

