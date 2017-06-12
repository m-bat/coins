 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */

	.text
	.align	4
	.global	_numcmp
_numcmp:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	pushl	%esi
	movl	8(%ebp),%eax
	movl	12(%ebp),%esi
	pushl	(%eax)
	call	_atoi
	movl	%eax,%ebx
	leal	4(%esp),%esp
	pushl	(%esi)
	call	_atoi
	leal	4(%esp),%esp
	subl	%eax,%ebx
	movl	%ebx,%eax
	popl	%esi
	popl	%ebx
	leave
	ret


	.align	4
	.global	_strcmp
_strcmp:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%ecx
	movl	12(%ebp),%eax
	movl	(%ecx),%edx
	movl	(%eax),%ecx
.L6:
	movb	(%edx),%al
	cmpb	(%ecx),%al
	jne	.L8
.L7:
	leal	1(%edx),%edx
	leal	1(%ecx),%ecx
	jmp	.L6
.L8:
	movsbl	(%edx),%eax
	movsbl	(%ecx),%ecx
	subl	%ecx,%eax
	leave
	ret

	.align	1
_string.18:
	.byte	37
	.byte	100
	.byte	0
	.align	1
_string.21:
	.byte	110
	.byte	117
	.byte	109
	.byte	101
	.byte	114
	.byte	105
	.byte	99
	.byte	32
	.byte	61
	.byte	32
	.byte	37
	.byte	100
	.byte	10
	.byte	0
	.align	1
_string.23:
	.byte	37
	.byte	115
	.byte	10
	.byte	0

	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$16,%esp
	pushl	%ebx
	movl	$0,%eax
	movl	$0,%ebx
.L12:
	cmpl	$100,%ebx
	jge	.L14
.L13:
	call	_rand
	pushl	%eax
	pushl	$_string.18
	leal	-16(%ebp),%eax
	pushl	%eax
	call	_sprintf
	leal	12(%esp),%esp
	leal	-16(%ebp),%eax
	pushl	%eax
	call	_strdup
	movl	%eax,_lineptr(,%ebx,4)
	leal	4(%esp),%esp
	leal	1(%ebx),%ebx
	jmp	.L12
.L14:
	movl	$0,%eax
	pushl	$0
	pushl	$_string.21
	call	_printf
	leal	8(%esp),%esp
	movl	$_strcmp,%eax
	pushl	%eax
	pushl	$4
	pushl	$100
	pushl	$_lineptr
	call	_qsort
	leal	16(%esp),%esp
	movl	$0,%ebx
.L18:
	cmpl	$100,%ebx
	jge	.L20
.L19:
	pushl	_lineptr(,%ebx,4)
	pushl	$_string.23
	call	_printf
	leal	8(%esp),%esp
	leal	1(%ebx),%ebx
	jmp	.L18
.L20:
	movl	$1,%eax
	pushl	$1
	pushl	$_string.21
	call	_printf
	leal	8(%esp),%esp
	movl	$_numcmp,%eax
	pushl	%eax
	pushl	$4
	pushl	$100
	pushl	$_lineptr
	call	_qsort
	leal	16(%esp),%esp
	movl	$0,%ebx
.L24:
	cmpl	$100,%ebx
	jge	.L26
.L25:
	pushl	_lineptr(,%ebx,4)
	pushl	$_string.23
	call	_printf
	leal	8(%esp),%esp
	leal	1(%ebx),%ebx
	jmp	.L24
.L26:
	movl	$0,%eax
	popl	%ebx
	leave
	ret

	.comm	_c,1
	.comm	_lineptr,20000
