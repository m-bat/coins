 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */

	.text
	.align	4
	.global	_swap
_swap:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	pushl	%esi
	movl	8(%ebp),%esi
	movl	12(%ebp),%ebx
	movl	16(%ebp),%edx
	movl	(%esi,%ebx,4),%ecx
	movl	(%esi,%edx,4),%eax
	movl	%eax,(%esi,%ebx,4)
	movl	%ecx,(%esi,%edx,4)
.L3:
	popl	%esi
	popl	%ebx
	leave
	ret


	.align	4
	.global	_selectFunc
_selectFunc:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%eax
	movl	12(%ebp),%ecx
	movl	16(%ebp),%edx
	cmpl	$1,%edx
	jne	.L7
.L6:
	jmp	.L8
.L7:
	movl	%ecx,%eax
.L8:
	leave
	ret


	.align	4
	.global	_qsort
_qsort:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$12,%esp
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	movl	8(%ebp),%eax
	movl	%eax,-12(%ebp)
	movl	12(%ebp),%ebx
	movl	16(%ebp),%eax
	movl	%eax,-8(%ebp)
	movl	20(%ebp),%eax
	movl	%eax,-4(%ebp)
	cmpl	-8(%ebp),%ebx
	jge	.L20
.L14:
	movl	%ebx,%eax
	addl	-8(%ebp),%eax
	movl	$2,%ecx
	cdq
	idivl	%ecx
	pushl	%eax
	pushl	%ebx
	pushl	-12(%ebp)
	call	_swap
	leal	12(%esp),%esp
	movl	%ebx,%esi
	leal	1(%ebx),%edi
.L15:
	cmpl	-8(%ebp),%edi
	jg	.L19
.L16:
	movl	-12(%ebp),%eax
	pushl	(%eax,%ebx,4)
	movl	-12(%ebp),%eax
	pushl	(%eax,%edi,4)
	movl	-4(%ebp),%eax
	call	*%eax
	leal	8(%esp),%esp
	cmpl	$0,%eax
	jge	.L18
.L17:
	leal	1(%esi),%esi
	pushl	%edi
	pushl	%esi
	pushl	-12(%ebp)
	call	_swap
	leal	12(%esp),%esp
.L18:
	leal	1(%edi),%edi
	jmp	.L15
.L19:
	pushl	%esi
	pushl	%ebx
	pushl	-12(%ebp)
	call	_swap
	leal	12(%esp),%esp
	pushl	-4(%ebp)
	leal	-1(%esi),%eax
	pushl	%eax
	pushl	%ebx
	pushl	-12(%ebp)
	call	_qsort
	leal	16(%esp),%esp
	pushl	-4(%ebp)
	pushl	-8(%ebp)
	leal	1(%esi),%eax
	pushl	%eax
	pushl	-12(%ebp)
	call	_qsort
	leal	16(%esp),%esp
.L20:
	popl	%edi
	popl	%esi
	popl	%ebx
	leave
	ret

	.align	1
_string.31:
	.byte	110
	.byte	117
	.byte	108
	.byte	108
	.byte	32
	.byte	114
	.byte	97
	.byte	110
	.byte	103
	.byte	101
	.byte	0
	.align	1
_string.33:
	.byte	109
	.byte	105
	.byte	110
	.byte	117
	.byte	115
	.byte	32
	.byte	114
	.byte	97
	.byte	110
	.byte	103
	.byte	101
	.byte	0

	.align	4
	.global	_qsort1
_qsort1:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	movl	8(%ebp),%edi
	movl	12(%ebp),%esi
	movl	16(%ebp),%ebx
	movl	20(%ebp),%edx
	movl	24(%ebp),%ecx
	movl	28(%ebp),%eax
	cmpl	%ebx,%esi
	jle	.L24
.L23:
	pushl	$_string.31
	call	*%ecx
	leal	4(%esp),%esp
	jmp	.L27
.L24:
	cmpl	$0,%esi
	jge	.L26
.L25:
	pushl	$_string.33
	movl	(%eax),%eax
	call	*%eax
	leal	4(%esp),%esp
	jmp	.L27
.L26:
	pushl	%edx
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	call	_qsort
	leal	16(%esp),%esp
.L27:
	popl	%edi
	popl	%esi
	popl	%ebx
	leave
	ret


	.align	4
	.global	_numcmp
_numcmp:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%ecx
	movl	12(%ebp),%eax
	cmpl	%eax,%ecx
	jge	.L33
.L32:
	movl	$-1,%eax
	jmp	.L36
.L33:
	cmpl	%eax,%ecx
	jle	.L35
.L34:
	movl	$1,%eax
	jmp	.L36
.L35:
	movl	$0,%eax
.L36:
	leave
	ret

	.align	1
_string.36:
	.byte	69
	.byte	114
	.byte	114
	.byte	111
	.byte	114
	.byte	32
	.byte	37
	.byte	115
	.byte	32
	.byte	10
	.byte	0

	.align	4
	.global	_error
_error:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%eax
	pushl	%eax
	pushl	$_string.36
	call	_printf
	leal	8(%esp),%esp
.L39:
	leave
	ret

	.align	1
_string.38:
	.byte	97
	.byte	98
	.byte	0
	.align	1
_string.39:
	.byte	99
	.byte	100
	.byte	0
	.align	1
_string.41:
	.byte	105
	.byte	49
	.byte	44
	.byte	105
	.byte	50
	.byte	44
	.byte	105
	.byte	51
	.byte	32
	.byte	61
	.byte	32
	.byte	37
	.byte	100
	.byte	44
	.byte	37
	.byte	100
	.byte	44
	.byte	37
	.byte	100
	.byte	10
	.byte	0

	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$8,%esp
	pushl	%ebx
	pushl	%esi
	leal	-8(%ebp),%eax
	movl	%eax,_data+4
	leal	-4(%ebp),%eax
	movl	%eax,_data+8
	movl	$_numcmp,_fptr1
	movl	$_numcmp,_fptr2
	pushl	$_string.39
	pushl	$_string.38
	call	_strcmp
	movl	%eax,%esi
	leal	8(%esp),%esp
	pushl	_data+8
	pushl	_data+4
	movl	_fptr1,%eax
	call	*%eax
	movl	%eax,%ebx
	leal	8(%esp),%esp
	pushl	_data+8
	pushl	_data+4
	movl	_fptr2,%eax
	call	*%eax
	leal	8(%esp),%esp
	pushl	%eax
	pushl	%ebx
	pushl	%esi
	pushl	$_string.41
	call	_printf
	leal	16(%esp),%esp
	pushl	$1
	pushl	$_strcmp
	pushl	$_numcmp
	call	_selectFunc
	leal	12(%esp),%esp
	pushl	%eax
	pushl	$100
	pushl	$0
	pushl	$_data
	call	_qsort
	leal	16(%esp),%esp
	movl	$0,%eax
	popl	%esi
	popl	%ebx
	leave
	ret

	.data
	.align	4
	.global	_numeric
_numeric:
	.long	0
	.comm	_fptr2,4
	.comm	_fptr1,4
	.comm	_c,1
	.comm	_data,20000
