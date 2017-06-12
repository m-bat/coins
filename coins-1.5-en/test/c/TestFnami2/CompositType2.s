 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */

	.text
	.align	4
	.global	_f0
_f0:
	pushl	%ebp
	movl	%esp,%ebp
	movb	$0,%al
	leave
	ret


	.align	4
	.global	_f1
_f1:
	pushl	%ebp
	movl	%esp,%ebp
	movb	$1,%al
	leave
	ret


	.align	4
	.global	_f2
_f2:
	pushl	%ebp
	movl	%esp,%ebp
	movb	$2,%al
	leave
	ret


	.align	4
	.global	_f3
_f3:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$8,%esp
	movl	8(%ebp),%eax
	fldl	12(%ebp)
	fstpl	-8(%ebp)
	movw	$3,%ax
	leave
	ret


	.align	4
	.global	_f4
_f4:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$8,%esp
	movl	8(%ebp),%eax
	fldl	12(%ebp)
	fstpl	-8(%ebp)
	movw	$4,%ax
	leave
	ret


	.align	4
	.global	_f5
_f5:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$8,%esp
	movl	8(%ebp),%eax
	fldl	12(%ebp)
	fstpl	-8(%ebp)
	movw	$5,%ax
	leave
	ret


	.align	4
	.global	_f6
_f6:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%eax
	movl	12(%ebp),%eax
	movl	$6,%eax
	leave
	ret


	.align	4
	.global	_f7
_f7:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%eax
	movl	12(%ebp),%eax
	movl	$7,%eax
	leave
	ret


	.align	4
	.global	_f8
_f8:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%eax
	movl	12(%ebp),%eax
	movl	$8,%eax
	leave
	ret


	.align	4
	.global	_f9
_f9:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%eax
	movl	12(%ebp),%eax
	movl	$9,%eax
	leave
	ret


	.align	4
	.global	_g6
_g6:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%eax
	movl	12(%ebp),%eax
	movl	$6,%eax
	leave
	ret


	.align	4
	.global	_g7
_g7:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%eax
	movl	12(%ebp),%eax
	movl	$7,%eax
	leave
	ret


	.align	4
	.global	_g8
_g8:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%eax
	movl	12(%ebp),%eax
	movl	$8,%eax
	leave
	ret


	.align	4
	.global	_g9
_g9:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%eax
	movl	12(%ebp),%eax
	movl	$9,%eax
	leave
	ret

	.align	1
_string.53:
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
	.align	1
_string.62:
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
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$44,%esp
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	movl	$_f0,%eax
	movl	$_f1,%edi
	movl	$_f2,%esi
	movl	$_f3,-4(%ebp)
	movl	$_f4,-8(%ebp)
	movl	$_f5,-12(%ebp)
	movl	$_f6,-28(%ebp)
	movl	$_f7,-24(%ebp)
	movl	$_f8,-20(%ebp)
	movl	$_f9,-16(%ebp)
	movl	$_g6,-44(%ebp)
	movl	$_g7,-40(%ebp)
	movl	$_g8,-36(%ebp)
	movl	$_g9,-32(%ebp)
	call	*%eax
	movb	%al,%bh
	call	*%edi
	movb	%al,%bl
	call	*%esi
	movsbl	%al,%eax
	pushl	%eax
	movsbl	%bl,%eax
	pushl	%eax
	movsbl	%bh,%eax
	pushl	%eax
	pushl	$_string.53
	call	_printf
	leal	16(%esp),%esp
	fldz
	sub	$8,%esp
	fstpl	(%esp)
	pushl	$0
	movl	-4(%ebp),%eax
	call	*%eax
	movw	%ax,%si
	leal	12(%esp),%esp
	fldz
	sub	$8,%esp
	fstpl	(%esp)
	pushl	$0
	movl	-8(%ebp),%eax
	call	*%eax
	movw	%ax,%bx
	leal	12(%esp),%esp
	fldz
	sub	$8,%esp
	fstpl	(%esp)
	pushl	$0
	movl	-12(%ebp),%eax
	call	*%eax
	leal	12(%esp),%esp
	movswl	%ax,%eax
	pushl	%eax
	movswl	%bx,%eax
	pushl	%eax
	movswl	%si,%eax
	pushl	%eax
	pushl	$_string.53
	call	_printf
	leal	16(%esp),%esp
	pushl	$0
	pushl	$0
	movl	-28(%ebp),%eax
	call	*%eax
	movl	%eax,%edi
	leal	8(%esp),%esp
	pushl	$0
	pushl	$0
	movl	-24(%ebp),%eax
	call	*%eax
	movl	%eax,%esi
	leal	8(%esp),%esp
	pushl	$0
	pushl	$0
	movl	-20(%ebp),%eax
	call	*%eax
	movl	%eax,%ebx
	leal	8(%esp),%esp
	pushl	$0
	pushl	$0
	movl	-16(%ebp),%eax
	call	*%eax
	leal	8(%esp),%esp
	pushl	%eax
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	pushl	$_string.62
	call	_printf
	leal	20(%esp),%esp
	pushl	$0
	pushl	$0
	movl	-44(%ebp),%eax
	call	*%eax
	movl	%eax,%edi
	leal	8(%esp),%esp
	pushl	$0
	pushl	$0
	movl	-40(%ebp),%eax
	call	*%eax
	movl	%eax,%esi
	leal	8(%esp),%esp
	pushl	$0
	pushl	$0
	movl	-36(%ebp),%eax
	call	*%eax
	movl	%eax,%ebx
	leal	8(%esp),%esp
	pushl	$0
	pushl	$0
	movl	-32(%ebp),%eax
	call	*%eax
	leal	8(%esp),%esp
	pushl	%eax
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	pushl	$_string.62
	call	_printf
	leal	20(%esp),%esp
	movl	$0,%eax
	popl	%edi
	popl	%esi
	popl	%ebx
	leave
	ret

