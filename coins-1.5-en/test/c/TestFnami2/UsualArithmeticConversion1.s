 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */

	.text
	.align	4
	.global	_f1d
_f1d:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$24,%esp
	pushl	%ebx
	movl	8(%ebp),%ebx
	movl	12(%ebp),%edx
	movl	16(%ebp),%ecx
	movl	20(%ebp),%eax
	flds	24(%ebp)
	fstps	-4(%ebp)
	movl	%edx,-24(%ebp)
	fildl	-24(%ebp)
	fstpl	-12(%ebp)
	cmpl	$0,%edx
	jge	.L34
.L35:
	fldl	-12(%ebp)
	faddl	.LC1
	fstpl	-12(%ebp)
.L34:
	movl	%eax,-24(%ebp)
	fildl	-24(%ebp)
	fstpl	-20(%ebp)
	cmpl	$0,%eax
	jge	.L36
.L37:
	fldl	-20(%ebp)
	faddl	.LC1
	fstpl	-20(%ebp)
.L36:
	fldl	.LC3
	flds	-4(%ebp)
	fmulp	%st,%st(1)
	sub	$8,%esp
	fstpl	(%esp)
	fldl	.LC2
	fmull	-20(%ebp)
	sub	$8,%esp
	fstpl	(%esp)
	fldl	.LC2
	movl	%ecx,-24(%ebp)
	fildl	-24(%ebp)
	fmulp	%st,%st(1)
	sub	$8,%esp
	fstpl	(%esp)
	fldl	.LC2
	fmull	-12(%ebp)
	sub	$8,%esp
	fstpl	(%esp)
	fldl	.LC2
	movl	%ebx,-24(%ebp)
	fildl	-24(%ebp)
	fmulp	%st,%st(1)
	sub	$8,%esp
	fstpl	(%esp)
	pushl	$_s5
	call	_printf
	leal	44(%esp),%esp
.L3:
	popl	%ebx
	leave
	ret


	.align	4
	.global	_f1f
_f1f:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$12,%esp
	pushl	%ebx
	movl	8(%ebp),%ebx
	movl	12(%ebp),%edx
	movl	16(%ebp),%ecx
	movl	20(%ebp),%eax
	movl	%edx,-12(%ebp)
	fildl	-12(%ebp)
	fstps	-4(%ebp)
	cmpl	$0,%edx
	jge	.L38
.L39:
	flds	-4(%ebp)
	fadds	.LC4
	fstps	-4(%ebp)
.L38:
	movl	%eax,-12(%ebp)
	fildl	-12(%ebp)
	fstps	-8(%ebp)
	cmpl	$0,%eax
	jge	.L40
.L41:
	flds	-8(%ebp)
	fadds	.LC4
	fstps	-8(%ebp)
.L40:
	flds	.LC6
	fmuls	-8(%ebp)
	sub	$8,%esp
	fstpl	(%esp)
	flds	.LC5
	movl	%ecx,-12(%ebp)
	fildl	-12(%ebp)
	fmulp	%st,%st(1)
	sub	$8,%esp
	fstpl	(%esp)
	flds	.LC6
	fmuls	-4(%ebp)
	sub	$8,%esp
	fstpl	(%esp)
	flds	.LC5
	movl	%ebx,-12(%ebp)
	fildl	-12(%ebp)
	fmulp	%st,%st(1)
	sub	$8,%esp
	fstpl	(%esp)
	pushl	$_s4
	call	_printf
	leal	36(%esp),%esp
.L6:
	popl	%ebx
	leave
	ret


	.align	4
	.global	_f1ul
_f1ul:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	pushl	%esi
	movl	8(%ebp),%ecx
	movl	12(%ebp),%ebx
	movl	16(%ebp),%esi
	movl	$-1294967292,%eax
	xorl	%edx,%edx
	divl	%esi
	pushl	%eax
	movl	$-1294967294,%eax
	xorl	%edx,%edx
	divl	%ebx
	pushl	%eax
	movl	$-1294967295,%eax
	xorl	%edx,%edx
	divl	%ecx
	pushl	%eax
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
.L9:
	popl	%esi
	popl	%ebx
	leave
	ret


	.align	4
	.global	_f1l
_f1l:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	movl	8(%ebp),%ecx
	movl	12(%ebp),%ebx
	movl	$-1000000001,%eax
	xorl	%edx,%edx
	divl	%ebx
	pushl	%eax
	movl	$-1000000000,%eax
	cdq
	idivl	%ecx
	pushl	%eax
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
.L12:
	popl	%ebx
	leave
	ret


	.align	4
	.global	_f1ui
_f1ui:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%ecx
	movl	$-1294967291,%eax
	xorl	%edx,%edx
	divl	%ecx
	pushl	%eax
	pushl	$_s1
	call	_printf
	leal	8(%esp),%esp
.L15:
	leave
	ret


	.align	4
	.global	_f2d
_f2d:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$24,%esp
	pushl	%ebx
	movl	8(%ebp),%ebx
	movl	12(%ebp),%edx
	movl	16(%ebp),%ecx
	movl	20(%ebp),%eax
	flds	24(%ebp)
	fstps	-4(%ebp)
	movl	%edx,-24(%ebp)
	fildl	-24(%ebp)
	fstpl	-12(%ebp)
	cmpl	$0,%edx
	jge	.L42
.L43:
	fldl	-12(%ebp)
	faddl	.LC1
	fstpl	-12(%ebp)
.L42:
	movl	%eax,-24(%ebp)
	fildl	-24(%ebp)
	fstpl	-20(%ebp)
	cmpl	$0,%eax
	jge	.L44
.L45:
	fldl	-20(%ebp)
	faddl	.LC1
	fstpl	-20(%ebp)
.L44:
	flds	-4(%ebp)
	fmull	.LC3
	sub	$8,%esp
	fstpl	(%esp)
	fldl	-20(%ebp)
	fmull	.LC2
	sub	$8,%esp
	fstpl	(%esp)
	movl	%ecx,-24(%ebp)
	fildl	-24(%ebp)
	fmull	.LC2
	sub	$8,%esp
	fstpl	(%esp)
	fldl	-12(%ebp)
	fmull	.LC2
	sub	$8,%esp
	fstpl	(%esp)
	movl	%ebx,-24(%ebp)
	fildl	-24(%ebp)
	fmull	.LC2
	sub	$8,%esp
	fstpl	(%esp)
	pushl	$_s5
	call	_printf
	leal	44(%esp),%esp
.L18:
	popl	%ebx
	leave
	ret


	.align	4
	.global	_f2f
_f2f:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$12,%esp
	pushl	%ebx
	movl	8(%ebp),%ebx
	movl	12(%ebp),%edx
	movl	16(%ebp),%ecx
	movl	20(%ebp),%eax
	movl	%edx,-12(%ebp)
	fildl	-12(%ebp)
	fstps	-4(%ebp)
	cmpl	$0,%edx
	jge	.L46
.L47:
	flds	-4(%ebp)
	fadds	.LC4
	fstps	-4(%ebp)
.L46:
	movl	%eax,-12(%ebp)
	fildl	-12(%ebp)
	fstps	-8(%ebp)
	cmpl	$0,%eax
	jge	.L48
.L49:
	flds	-8(%ebp)
	fadds	.LC4
	fstps	-8(%ebp)
.L48:
	flds	-8(%ebp)
	fmuls	.LC6
	sub	$8,%esp
	fstpl	(%esp)
	movl	%ecx,-12(%ebp)
	fildl	-12(%ebp)
	fmuls	.LC5
	sub	$8,%esp
	fstpl	(%esp)
	flds	-4(%ebp)
	fmuls	.LC6
	sub	$8,%esp
	fstpl	(%esp)
	movl	%ebx,-12(%ebp)
	fildl	-12(%ebp)
	fmuls	.LC5
	sub	$8,%esp
	fstpl	(%esp)
	pushl	$_s4
	call	_printf
	leal	36(%esp),%esp
.L21:
	popl	%ebx
	leave
	ret


	.align	4
	.global	_f2ul
_f2ul:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	pushl	%esi
	movl	8(%ebp),%ecx
	movl	12(%ebp),%ebx
	movl	16(%ebp),%eax
	movl	$3,%esi
	xorl	%edx,%edx
	divl	%esi
	pushl	%eax
	movl	$3,%esi
	movl	%ebx,%eax
	xorl	%edx,%edx
	divl	%esi
	pushl	%eax
	movl	$3,%ebx
	movl	%ecx,%eax
	xorl	%edx,%edx
	divl	%ebx
	pushl	%eax
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
.L24:
	popl	%esi
	popl	%ebx
	leave
	ret


	.align	4
	.global	_f2l
_f2l:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	movl	8(%ebp),%ecx
	movl	12(%ebp),%eax
	movl	$3,%ebx
	xorl	%edx,%edx
	divl	%ebx
	pushl	%eax
	movl	$3,%ebx
	movl	%ecx,%eax
	cdq
	idivl	%ebx
	pushl	%eax
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
.L27:
	popl	%ebx
	leave
	ret


	.align	4
	.global	_f2ui
_f2ui:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%eax
	movl	$3,%ecx
	xorl	%edx,%edx
	divl	%ecx
	pushl	%eax
	pushl	$_s1
	call	_printf
	leal	8(%esp),%esp
.L30:
	leave
	ret


	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$120,%esp
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	pushl	_d_f+4
	pushl	_d_f
	pushl	_d_ul+4
	pushl	_d_ul
	pushl	_d_l+4
	pushl	_d_l
	pushl	_d_ui+4
	pushl	_d_ui
	pushl	_d_i+4
	pushl	_d_i
	pushl	$_s5
	call	_printf
	leal	44(%esp),%esp
	movl	$-11,-116(%ebp)
	movl	$-1194967296,%edi
	movl	$-21,%esi
	movl	$-1094967296,%ebx
	flds	.LC7
	fstps	-4(%ebp)
	fldl	.LC8
	fstpl	-12(%ebp)
	fldl	-12(%ebp)
	faddl	.LC1
	fstpl	-12(%ebp)
	movl	%ebx,-120(%ebp)
	fildl	-120(%ebp)
	fstpl	-20(%ebp)
	cmpl	$0,%ebx
	jge	.L52
.L53:
	fldl	-20(%ebp)
	faddl	.LC1
	fstpl	-20(%ebp)
.L52:
	fldl	.LC3
	flds	-4(%ebp)
	fmulp	%st,%st(1)
	sub	$8,%esp
	fstpl	(%esp)
	fldl	.LC2
	fmull	-20(%ebp)
	sub	$8,%esp
	fstpl	(%esp)
	fldl	.LC2
	movl	%esi,-120(%ebp)
	fildl	-120(%ebp)
	fmulp	%st,%st(1)
	sub	$8,%esp
	fstpl	(%esp)
	fldl	.LC2
	fmull	-12(%ebp)
	sub	$8,%esp
	fstpl	(%esp)
	fldl	.LC2
	fildl	-116(%ebp)
	fmulp	%st,%st(1)
	sub	$8,%esp
	fstpl	(%esp)
	pushl	$_s5
	call	_printf
	leal	44(%esp),%esp
	movl	%edi,-120(%ebp)
	fildl	-120(%ebp)
	fstpl	-28(%ebp)
	cmpl	$0,%edi
	jge	.L54
.L55:
	fldl	-28(%ebp)
	faddl	.LC1
	fstpl	-28(%ebp)
.L54:
	movl	%ebx,-120(%ebp)
	fildl	-120(%ebp)
	fstpl	-36(%ebp)
	cmpl	$0,%ebx
	jge	.L56
.L57:
	fldl	-36(%ebp)
	faddl	.LC1
	fstpl	-36(%ebp)
.L56:
	fldl	.LC3
	flds	-4(%ebp)
	fmulp	%st,%st(1)
	sub	$8,%esp
	fstpl	(%esp)
	fldl	.LC2
	fmull	-36(%ebp)
	sub	$8,%esp
	fstpl	(%esp)
	fldl	.LC2
	movl	%esi,-120(%ebp)
	fildl	-120(%ebp)
	fmulp	%st,%st(1)
	sub	$8,%esp
	fstpl	(%esp)
	fldl	.LC2
	fmull	-28(%ebp)
	sub	$8,%esp
	fstpl	(%esp)
	fldl	.LC2
	fildl	-116(%ebp)
	fmulp	%st,%st(1)
	sub	$8,%esp
	fstpl	(%esp)
	pushl	$_s5
	call	_printf
	leal	44(%esp),%esp
	pushl	.LC7
	pushl	$-1094967296
	pushl	$-21
	pushl	$-1194967296
	pushl	$-11
	call	_f1d
	leal	20(%esp),%esp
	flds	_f_ul
	sub	$8,%esp
	fstpl	(%esp)
	flds	_f_l
	sub	$8,%esp
	fstpl	(%esp)
	flds	_f_ui
	sub	$8,%esp
	fstpl	(%esp)
	flds	_f_i
	sub	$8,%esp
	fstpl	(%esp)
	pushl	$_s4
	call	_printf
	leal	36(%esp),%esp
	movl	$-11,-112(%ebp)
	movl	$-1194967296,%edi
	movl	$-21,%esi
	movl	$-1094967296,%ebx
	flds	.LC9
	fstps	-40(%ebp)
	flds	-40(%ebp)
	fadds	.LC4
	fstps	-40(%ebp)
	movl	%ebx,-120(%ebp)
	fildl	-120(%ebp)
	fstps	-44(%ebp)
	cmpl	$0,%ebx
	jge	.L60
.L61:
	flds	-44(%ebp)
	fadds	.LC4
	fstps	-44(%ebp)
.L60:
	flds	.LC6
	fmuls	-44(%ebp)
	sub	$8,%esp
	fstpl	(%esp)
	flds	.LC5
	movl	%esi,-120(%ebp)
	fildl	-120(%ebp)
	fmulp	%st,%st(1)
	sub	$8,%esp
	fstpl	(%esp)
	flds	.LC6
	fmuls	-40(%ebp)
	sub	$8,%esp
	fstpl	(%esp)
	flds	.LC5
	fildl	-112(%ebp)
	fmulp	%st,%st(1)
	sub	$8,%esp
	fstpl	(%esp)
	pushl	$_s4
	call	_printf
	leal	36(%esp),%esp
	movl	%edi,-120(%ebp)
	fildl	-120(%ebp)
	fstps	-48(%ebp)
	cmpl	$0,%edi
	jge	.L62
.L63:
	flds	-48(%ebp)
	fadds	.LC4
	fstps	-48(%ebp)
.L62:
	movl	%ebx,-120(%ebp)
	fildl	-120(%ebp)
	fstps	-52(%ebp)
	cmpl	$0,%ebx
	jge	.L64
.L65:
	flds	-52(%ebp)
	fadds	.LC4
	fstps	-52(%ebp)
.L64:
	flds	.LC6
	fmuls	-52(%ebp)
	sub	$8,%esp
	fstpl	(%esp)
	flds	.LC5
	movl	%esi,-120(%ebp)
	fildl	-120(%ebp)
	fmulp	%st,%st(1)
	sub	$8,%esp
	fstpl	(%esp)
	flds	.LC6
	fmuls	-48(%ebp)
	sub	$8,%esp
	fstpl	(%esp)
	flds	.LC5
	fildl	-112(%ebp)
	fmulp	%st,%st(1)
	sub	$8,%esp
	fstpl	(%esp)
	pushl	$_s4
	call	_printf
	leal	36(%esp),%esp
	pushl	$-1094967296
	pushl	$-21
	pushl	$-1194967296
	pushl	$-11
	call	_f1f
	leal	16(%esp),%esp
	pushl	_ul_l
	pushl	_ul_ui
	pushl	_ul_i
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	movl	$3,%eax
	movl	$3,%eax
	movl	$3,%eax
	pushl	$1000000001
	pushl	$1000000000
	pushl	$1000000000
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	pushl	$1000000001
	pushl	$1000000000
	pushl	$1000000000
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	pushl	$3
	pushl	$3
	pushl	$3
	call	_f1ul
	leal	12(%esp),%esp
	pushl	_l_ui
	pushl	_l_i
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
	movl	$3,%eax
	movl	$3,%eax
	pushl	$1098322431
	pushl	$-333333333
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
	pushl	$1098322431
	pushl	$-333333333
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
	pushl	$3
	pushl	$3
	call	_f1l
	leal	8(%esp),%esp
	pushl	_ui_i
	pushl	$_s1
	call	_printf
	leal	8(%esp),%esp
	movl	$3,%eax
	pushl	$1000000001
	pushl	$_s1
	call	_printf
	leal	8(%esp),%esp
	pushl	$1000000001
	pushl	$_s1
	call	_printf
	leal	8(%esp),%esp
	pushl	$3
	call	_f1ui
	leal	4(%esp),%esp
	pushl	_f_d+4
	pushl	_f_d
	pushl	_ul_d+4
	pushl	_ul_d
	pushl	_l_d+4
	pushl	_l_d
	pushl	_ui_d+4
	pushl	_ui_d
	pushl	_i_d+4
	pushl	_i_d
	pushl	$_s5
	call	_printf
	leal	44(%esp),%esp
	movl	$-12,-108(%ebp)
	movl	$-994967296,%edi
	movl	$-22,%esi
	movl	$-894967296,%ebx
	flds	.LC7
	fstps	-4(%ebp)
	fldl	.LC10
	fstpl	-60(%ebp)
	fldl	-60(%ebp)
	faddl	.LC1
	fstpl	-60(%ebp)
	movl	%ebx,-120(%ebp)
	fildl	-120(%ebp)
	fstpl	-68(%ebp)
	cmpl	$0,%ebx
	jge	.L68
.L69:
	fldl	-68(%ebp)
	faddl	.LC1
	fstpl	-68(%ebp)
.L68:
	flds	-4(%ebp)
	fmull	.LC3
	sub	$8,%esp
	fstpl	(%esp)
	fldl	-68(%ebp)
	fmull	.LC2
	sub	$8,%esp
	fstpl	(%esp)
	movl	%esi,-120(%ebp)
	fildl	-120(%ebp)
	fmull	.LC2
	sub	$8,%esp
	fstpl	(%esp)
	fldl	-60(%ebp)
	fmull	.LC2
	sub	$8,%esp
	fstpl	(%esp)
	fildl	-108(%ebp)
	fmull	.LC2
	sub	$8,%esp
	fstpl	(%esp)
	pushl	$_s5
	call	_printf
	leal	44(%esp),%esp
	movl	%edi,-120(%ebp)
	fildl	-120(%ebp)
	fstpl	-76(%ebp)
	cmpl	$0,%edi
	jge	.L70
.L71:
	fldl	-76(%ebp)
	faddl	.LC1
	fstpl	-76(%ebp)
.L70:
	movl	%ebx,-120(%ebp)
	fildl	-120(%ebp)
	fstpl	-84(%ebp)
	cmpl	$0,%ebx
	jge	.L72
.L73:
	fldl	-84(%ebp)
	faddl	.LC1
	fstpl	-84(%ebp)
.L72:
	flds	-4(%ebp)
	fmull	.LC3
	sub	$8,%esp
	fstpl	(%esp)
	fldl	-84(%ebp)
	fmull	.LC2
	sub	$8,%esp
	fstpl	(%esp)
	movl	%esi,-120(%ebp)
	fildl	-120(%ebp)
	fmull	.LC2
	sub	$8,%esp
	fstpl	(%esp)
	fldl	-76(%ebp)
	fmull	.LC2
	sub	$8,%esp
	fstpl	(%esp)
	fildl	-108(%ebp)
	fmull	.LC2
	sub	$8,%esp
	fstpl	(%esp)
	pushl	$_s5
	call	_printf
	leal	44(%esp),%esp
	pushl	.LC7
	pushl	$-894967296
	pushl	$-22
	pushl	$-994967296
	pushl	$-12
	call	_f2d
	leal	20(%esp),%esp
	flds	_ul_f
	sub	$8,%esp
	fstpl	(%esp)
	flds	_l_f
	sub	$8,%esp
	fstpl	(%esp)
	flds	_ui_f
	sub	$8,%esp
	fstpl	(%esp)
	flds	_i_f
	sub	$8,%esp
	fstpl	(%esp)
	pushl	$_s4
	call	_printf
	leal	36(%esp),%esp
	movl	$-12,-104(%ebp)
	movl	$-994967296,%edi
	movl	$-22,%esi
	movl	$-894967296,%ebx
	flds	.LC11
	fstps	-88(%ebp)
	flds	-88(%ebp)
	fadds	.LC4
	fstps	-88(%ebp)
	movl	%ebx,-120(%ebp)
	fildl	-120(%ebp)
	fstps	-92(%ebp)
	cmpl	$0,%ebx
	jge	.L76
.L77:
	flds	-92(%ebp)
	fadds	.LC4
	fstps	-92(%ebp)
.L76:
	flds	-92(%ebp)
	fmuls	.LC6
	sub	$8,%esp
	fstpl	(%esp)
	movl	%esi,-120(%ebp)
	fildl	-120(%ebp)
	fmuls	.LC5
	sub	$8,%esp
	fstpl	(%esp)
	flds	-88(%ebp)
	fmuls	.LC6
	sub	$8,%esp
	fstpl	(%esp)
	fildl	-104(%ebp)
	fmuls	.LC5
	sub	$8,%esp
	fstpl	(%esp)
	pushl	$_s4
	call	_printf
	leal	36(%esp),%esp
	movl	%edi,-120(%ebp)
	fildl	-120(%ebp)
	fstps	-96(%ebp)
	cmpl	$0,%edi
	jge	.L78
.L79:
	flds	-96(%ebp)
	fadds	.LC4
	fstps	-96(%ebp)
.L78:
	movl	%ebx,-120(%ebp)
	fildl	-120(%ebp)
	fstps	-100(%ebp)
	cmpl	$0,%ebx
	jge	.L80
.L81:
	flds	-100(%ebp)
	fadds	.LC4
	fstps	-100(%ebp)
.L80:
	flds	-100(%ebp)
	fmuls	.LC6
	sub	$8,%esp
	fstpl	(%esp)
	movl	%esi,-120(%ebp)
	fildl	-120(%ebp)
	fmuls	.LC5
	sub	$8,%esp
	fstpl	(%esp)
	flds	-96(%ebp)
	fmuls	.LC6
	sub	$8,%esp
	fstpl	(%esp)
	fildl	-104(%ebp)
	fmuls	.LC5
	sub	$8,%esp
	fstpl	(%esp)
	pushl	$_s4
	call	_printf
	leal	36(%esp),%esp
	pushl	$-894967296
	pushl	$-22
	pushl	$-994967296
	pushl	$-12
	call	_f2f
	leal	16(%esp),%esp
	pushl	_l_ul
	pushl	_ui_ul
	pushl	_i_ul
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	movl	$-1000000002,%eax
	movl	$-1294967289,%eax
	movl	$-1000000004,%eax
	pushl	$1098322430
	pushl	$1000000002
	pushl	$1098322431
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	pushl	$1098322430
	pushl	$1000000002
	pushl	$1098322431
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	pushl	$-1000000004
	pushl	$-1294967289
	pushl	$-1000000002
	call	_f2ul
	leal	12(%esp),%esp
	pushl	_ui_l
	pushl	_i_l
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
	movl	$-1000000006,%eax
	movl	$-1294967288,%eax
	pushl	$1000000002
	pushl	$-333333335
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
	pushl	$1000000002
	pushl	$-333333335
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
	pushl	$-1294967288
	pushl	$-1000000006
	call	_f2l
	leal	8(%esp),%esp
	pushl	_i_ui
	pushl	$_s1
	call	_printf
	leal	8(%esp),%esp
	movl	$-1000000007,%eax
	pushl	$1098322429
	pushl	$_s1
	call	_printf
	leal	8(%esp),%esp
	pushl	$1098322429
	pushl	$_s1
	call	_printf
	leal	8(%esp),%esp
	pushl	$-1000000007
	call	_f2ui
	leal	4(%esp),%esp
	movl	$0,%eax
	popl	%edi
	popl	%esi
	popl	%ebx
	leave
	ret

	.data
	.align	1
	.global	_s5
_s5:
	.byte	37
	.byte	103
	.byte	32
	.byte	37
	.byte	103
	.byte	32
	.byte	37
	.byte	103
	.byte	32
	.byte	37
	.byte	103
	.byte	32
	.byte	37
	.byte	103
	.byte	10
	.byte	0
	.align	1
	.global	_s4
_s4:
	.byte	37
	.byte	103
	.byte	32
	.byte	37
	.byte	103
	.byte	32
	.byte	37
	.byte	103
	.byte	32
	.byte	37
	.byte	103
	.byte	10
	.byte	0
	.align	1
	.global	_s3
_s3:
	.byte	37
	.byte	108
	.byte	117
	.byte	32
	.byte	37
	.byte	108
	.byte	117
	.byte	32
	.byte	37
	.byte	108
	.byte	117
	.byte	10
	.byte	0
	.align	1
	.global	_s2
_s2:
	.byte	37
	.byte	108
	.byte	100
	.byte	32
	.byte	37
	.byte	108
	.byte	117
	.byte	10
	.byte	0
	.align	1
	.global	_s1
_s1:
	.byte	37
	.byte	117
	.byte	10
	.byte	0
	.align	4
	.global	_d_i
_d_i:
	.long	0x59df1e53,0xc809dc62 /* -1.0999999999999999E39 */
	.align	4
	.global	_d_ui
_d_ui:
	.long	0xbcf19228,0x49cb2670 /* 3.1E47 */
	.align	4
	.global	_d_l
_d_l:
	.long	0x273db438,0xc818af75 /* -2.1E39 */
	.align	4
	.global	_d_ul
_d_ul:
	.long	0xec5433c6,0x49cc06a5 /* 3.2E47 */
	.align	4
	.global	_d_f
_d_f:
	.long	0x94e33440,0x49e5e531 /* 9.999999680285692E47 */
	.align	4
	.global	_f_i
_f_i:
	.long	0xfea58275 /* -1.0999999927197394E38 */
	.align	4
	.global	_f_ui
_f_ui:
	.long	0x7f6937d4 /* 3.1000000466471453E38 */
	.align	4
	.global	_f_l
_f_l:
	.long	0xff1dfc87 /* -2.0999999861013206E38 */
	.align	4
	.global	_f_ul
_f_ul:
	.long	0x7f70bdc2 /* 3.200000048151892E38 */
	.align	4
	.global	_ul_i
_ul_i:
	.long	1000000000
	.align	4
	.global	_ul_ui
_ul_ui:
	.long	1000000000
	.align	4
	.global	_ul_l
_ul_l:
	.long	1000000001
	.align	4
	.global	_l_i
_l_i:
	.long	-333333333
	.align	4
	.global	_l_ui
_l_ui:
	.long	1098322431
	.align	4
	.global	_ui_i
_ui_i:
	.long	1000000001
	.align	4
	.global	_i_d
_i_d:
	.long	0xbf21f28a,0xc80c363c /* -1.2E39 */
	.align	4
	.global	_ui_d
_ui_d:
	.long	0x1bb6d564,0x49cce6db /* 3.3E47 */
	.align	4
	.global	_l_d
_l_d:
	.long	0x59df1e53,0xc819dc62 /* -2.1999999999999998E39 */
	.align	4
	.global	_ul_d
_ul_d:
	.long	0x4b197702,0x49cdc710 /* 3.4E47 */
	.align	4
	.global	_f_d
_f_d:
	.long	0x94e33440,0x49e5e531 /* 9.999999680285692E47 */
	.align	4
	.global	_i_f
_i_f:
	.long	0xfeb48e52 /* -1.1999999920578975E38 */
	.align	4
	.global	_ui_f
_ui_f:
	.long	0x7f7843b0 /* 3.3000000496566385E38 */
	.align	4
	.global	_l_f
_l_f:
	.long	0xff258275 /* -2.1999999854394788E38 */
	.align	4
	.global	_ul_f
_ul_f:
	.long	0x7f7fc99e /* 3.400000051161385E38 */
	.align	4
	.global	_i_ul
_i_ul:
	.long	1098322431
	.align	4
	.global	_ui_ul
_ui_ul:
	.long	1000000002
	.align	4
	.global	_l_ul
_l_ul:
	.long	1098322430
	.align	4
	.global	_i_l
_i_l:
	.long	-333333335
	.align	4
	.global	_ui_l
_ui_l:
	.long	1000000002
	.align	4
	.global	_i_ui
_i_ui:
	.long	1098322429
	.text
	.align	4
.LC1:
	.long	0x0,0x41f00000 /* 4.294967296E9 */
	.align	4
.LC2:
	.long	0x2a16a1b1,0x47d2ced3 /* 1.0E38 */
	.align	4
.LC3:
	.long	0x20000000,0x4202a05f /* 1.0E10 */
	.align	4
.LC4:
	.long	0x4f800000 /* 4.294967296E9 */
	.align	4
.LC5:
	.long	0x7cf0bdc2 /* 9.999999933815813E36 */
	.align	4
.LC6:
	.long	0x6fa18f08 /* 1.0000000150474662E29 */
	.align	4
.LC7:
	.long	0x7e967699 /* 9.999999680285692E37 */
	.align	4
.LC8:
	.long	0x40000000,0xc1d1ce70 /* -1.194967296E9 */
	.align	4
.LC9:
	.long	0xce8e7382 /* -1.194967296E9 */
	.align	4
.LC10:
	.long	0x80000000,0xc1cda6ff /* -9.94967296E8 */
	.align	4
.LC11:
	.long	0xce6d37fc /* -9.94967296E8 */
