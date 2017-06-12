 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */
	.text
	.align	1
_string.6:
	.byte	99
	.byte	104
	.byte	91
	.byte	37
	.byte	100
	.byte	93
	.byte	61
	.byte	37
	.byte	99
	.byte	32
	.byte	0
	.align	1
_string.8:
	.byte	10
	.byte	0

	.align	4
	.global	_print
_print:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	pushl	%esi
	movl	8(%ebp),%esi
	movl	$0,%ebx
.L3:
	cmpl	$12,%ebx
	jge	.L5
.L4:
	movsbl	(%esi,%ebx),%eax
	pushl	%eax
	pushl	%ebx
	pushl	$_string.6
	call	_printf
	leal	12(%esp),%esp
	leal	1(%ebx),%ebx
	jmp	.L3
.L5:
	pushl	$_string.8
	call	_printf
	leal	4(%esp),%esp
.L6:
	popl	%esi
	popl	%ebx
	leave
	ret

	.align	1
_string.10:
	.byte	104
	.byte	101
	.byte	108
	.byte	108
	.byte	111
	.byte	32
	.byte	119
	.byte	111
	.byte	114
	.byte	108
	.byte	100
	.byte	0

	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$24,%esp
	pushl	%ebx
	movl	$_string.10,%ebx
	leal	-12(%ebp),%edx
	movl	$12,%ecx
.L11:
	movb	(%ebx),%al
	movb	%al,(%edx)
	leal	1(%ebx),%ebx
	leal	1(%edx),%edx
	leal	-1(%ecx),%ecx
	cmpl	$0,%ecx
	jne	.L11
.L12:
	leal	-12(%ebp),%eax
	pushl	%eax
	call	_print
	leal	4(%esp),%esp
	movl	$_string.10,%ebx
	leal	-24(%ebp),%edx
	movl	$12,%ecx
.L13:
	movb	(%ebx),%al
	movb	%al,(%edx)
	leal	1(%ebx),%ebx
	leal	1(%edx),%edx
	leal	-1(%ecx),%ecx
	cmpl	$0,%ecx
	jne	.L13
.L14:
	leal	-24(%ebp),%eax
	pushl	%eax
	call	_print
	leal	4(%esp),%esp
.L9:
	popl	%ebx
	leave
	ret

