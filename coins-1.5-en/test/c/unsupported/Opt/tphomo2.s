 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-0.8.1"
/* JavaCG for target:x86 convention:cygwin */

	.text
	.align	4
	.global	_ProcSemi
_ProcSemi:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$120,%esp
	pushl	%ebx
	pushl	%esi
	leal	-40(%ebp),%edx
	movl	$123,(%edx)
	leal	-76(%ebp),%ecx
	movl	$234,(%ecx)
	leal	-116(%ebp),%esi
	movl	$345,(%esi)
	movl	_x,%ebx
	movl	(%edx),%eax
	leal	(%eax,%ebx,2),%eax
	movl	%eax,_x
	movl	_y,%eax
	imull	$3,%eax
	addl	(%ecx),%eax
	movl	%eax,_y
	movl	_z,%ebx
	movl	(%esi),%eax
	leal	(%eax,%ebx,2),%eax
	movl	%eax,_z
	movl	_x,%ebx
	movl	(%edx),%eax
	leal	(%ebx,%eax,2),%eax
	movl	%eax,_x
	movl	_y,%eax
	addl	(%ecx),%eax
	movl	%eax,_y
	movl	(%esi),%eax
	negl	%eax
	addl	_z,%eax
	movl	%eax,_z
	movl	_x,%esi
	movl	_y,%ebx
	cmpl	%ebx,%esi
	jge	.L8
.L3:
	cmpl	%eax,%ebx
	jge	.L8
.L4:
	cmpl	$0,%esi
	jge	.L8
.L5:
	movl	$0,(%edx)
.L8:
	movl	_x,%ebx
	movl	_z,%eax
	cmpl	%eax,%ebx
	jge	.L14
.L9:
	cmpl	_y,%ebx
	jle	.L14
.L10:
	cmpl	$0,%eax
	jge	.L14
.L11:
	movl	$0,-80(%ebp)
.L14:
	movl	_z,%eax
	addl	(%edx),%eax
	addl	(%ecx),%eax
	movl	%eax,_z
.L15:
	popl	%esi
	popl	%ebx
	leave
	ret


	.align	4
	.global	_Funcx
_Funcx:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$16,%esp
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	movl	8(%ebp),%eax
	movl	%eax,-12(%ebp)
	movl	12(%ebp),%eax
	movl	%eax,-4(%ebp)
	movl	-4(%ebp),%eax
	leal	32(%eax),%eax
	movl	%eax,-8(%ebp)
	movl	-12(%ebp),%eax
	movl	(%eax),%eax
	movl	%eax,-16(%ebp)
	movl	$0,%esi
	movl	-16(%ebp),%ecx
	movl	-12(%ebp),%edi
	movl	-4(%ebp),%edx
.L87:
	movl	%edx,%ebx
	movl	%ecx,%eax
	leal	4(%ebx),%edx
	leal	4(%edi),%edi
	movl	(%edi),%ecx
	leal	(%eax,%ecx),%eax
	shrl	$1,%eax
	subl	(%ebx),%eax
	cmpl	$0,%eax
	jb	.L21
.L20:
	leal	(%esi,%eax),%esi
	jmp	.L83
.L21:
	subl	%eax,%esi
.L83:
	cmpl	-8(%ebp),%edx
	jl	.L87
.L86:
	movl	$0,%edx
.L88:
	movl	-4(%ebp),%ebx
	movl	-16(%ebp),%eax
	leal	4(%ebx),%ecx
	movl	%ecx,-4(%ebp)
	movl	-12(%ebp),%ecx
	leal	4(%ecx),%ecx
	movl	%ecx,-12(%ebp)
	movl	-12(%ebp),%ecx
	movl	(%ecx),%ecx
	movl	%ecx,-16(%ebp)
	addl	-16(%ebp),%eax
	shrl	$1,%eax
	subl	(%ebx),%eax
	cmpl	$0,%eax
	jb	.L27
.L26:
	leal	(%edx,%eax),%edx
	jmp	.L84
.L27:
	subl	%eax,%edx
.L84:
	movl	-4(%ebp),%eax
	cmpl	-8(%ebp),%eax
	jl	.L88
.L29:
	leal	(%esi,%edx),%eax
	popl	%edi
	popl	%esi
	popl	%ebx
	leave
	ret


	.align	4
	.global	_Func
_Func:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%edx
	movl	12(%ebp),%eax
	movl	16(%ebp),%ecx
	leal	(%edx,%eax),%eax
	leal	(%eax,%ecx),%eax
	leave
	ret


	.align	4
	.global	_MakeRGBbuffer
_MakeRGBbuffer:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	movl	$0,%edx
	movl	$-4,%esi
	movl	$0,%edi
.L93:
	movl	%edi,%eax
	movl	%esi,%ecx
	movl	_rgbbuf,%ebx
	movb	$0,(%ebx,%edx)
	leal	8(%eax),%edi
	leal	4(%ecx),%esi
	leal	4(%edx),%edx
	movl	%edx,%ebx
	addl	_rgbbuf,%ebx
	movb	$0,1(%ebx)
	addl	_rgbbuf,%ecx
	movb	$0,2(%ecx)
	addl	_rgbbuf,%eax
	movb	$0,3(%eax)
	cmpl	$128,%edi
	jl	.L93
.L38:
	movl	_rgbp,%eax
	movb	$1,(%eax)
	movl	_rgbp,%eax
	movb	$2,1(%eax)
	movl	_rgbp,%eax
	movb	$3,2(%eax)
	movl	_rgbp,%eax
	movb	$4,3(%eax)
.L39:
	popl	%edi
	popl	%esi
	popl	%ebx
	leave
	ret

	.align	1
_string.55:
	.byte	120
	.byte	61
	.byte	37
	.byte	100
	.byte	32
	.byte	121
	.byte	61
	.byte	37
	.byte	100
	.byte	32
	.byte	122
	.byte	61
	.byte	37
	.byte	100
	.byte	32
	.byte	10
	.byte	0
	.align	1
_string.57:
	.byte	84
	.byte	97
	.byte	110
	.byte	97
	.byte	107
	.byte	97
	.byte	0
	.align	1
_string.59:
	.byte	83
	.byte	117
	.byte	122
	.byte	117
	.byte	107
	.byte	105
	.byte	0
	.align	1
_string.61:
	.byte	37
	.byte	115
	.byte	9
	.byte	37
	.byte	100
	.byte	32
	.byte	37
	.byte	100
	.byte	32
	.byte	37
	.byte	100
	.byte	9
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
_string.64:
	.byte	120
	.byte	61
	.byte	37
	.byte	100
	.byte	32
	.byte	10
	.byte	0
	.align	1
_string.66:
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
	subl	$316,%esp
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	movl	$1,-68(%ebp)
	movl	$2,-64(%ebp)
	movl	$3,-60(%ebp)
	movl	$4,-56(%ebp)
	movl	$5,-52(%ebp)
	movl	$6,-48(%ebp)
	movl	$7,-44(%ebp)
	movl	$8,-40(%ebp)
	movl	$9,-36(%ebp)
	leal	-32(%ebp),%esi
	leal	-28(%ebp),%ebx
	leal	-24(%ebp),%edx
	leal	-20(%ebp),%ecx
	leal	-100(%ebp),%eax
	movl	%eax,-292(%ebp)
	leal	-96(%ebp),%eax
	movl	%eax,-288(%ebp)
	leal	-92(%ebp),%eax
	movl	%eax,-284(%ebp)
	leal	-88(%ebp),%eax
	movl	%eax,-280(%ebp)
	movl	%ebp,%edi
	leal	-68(%ebp),%eax
	movl	%eax,-272(%ebp)
	movl	$9,%eax
.L43:
	movl	$0,(%esi)
	movl	$0,(%ebx)
	movl	$0,(%edx)
	movl	$0,(%ecx)
	leal	4(%eax),%eax
	leal	16(%ecx),%ecx
	leal	16(%edx),%edx
	leal	16(%ebx),%ebx
	leal	16(%esi),%esi
	cmpl	%edi,%ecx
	jl	.L43
.L44:
	leal	-68(%ebp,%eax,4),%ecx
	cmpl	$17,%eax
	jge	.L46
.L45:
	movl	$0,(%ecx)
	leal	4(%ecx),%ecx
	cmpl	%edi,%ecx
	jl	.L45
.L46:
	movl	$10,-132(%ebp)
	movl	$11,-128(%ebp)
	movl	$12,-124(%ebp)
	movl	$13,-120(%ebp)
	movl	$14,-116(%ebp)
	movl	$15,-112(%ebp)
	movl	$16,-108(%ebp)
	movl	$17,-104(%ebp)
	movl	$8,%ecx
.L48:
	movl	-292(%ebp),%eax
	movl	$0,(%eax)
	movl	-288(%ebp),%eax
	movl	$0,(%eax)
	movl	-284(%ebp),%eax
	movl	$0,(%eax)
	movl	-280(%ebp),%eax
	movl	$0,(%eax)
	leal	4(%ecx),%ecx
	movl	-280(%ebp),%eax
	leal	16(%eax),%eax
	movl	%eax,-280(%ebp)
	movl	-284(%ebp),%eax
	leal	16(%eax),%eax
	movl	%eax,-284(%ebp)
	movl	-288(%ebp),%eax
	leal	16(%eax),%eax
	movl	%eax,-288(%ebp)
	movl	-292(%ebp),%eax
	leal	16(%eax),%eax
	movl	%eax,-292(%ebp)
	movl	-280(%ebp),%eax
	cmpl	-272(%ebp),%eax
	jl	.L48
.L49:
	leal	-132(%ebp,%ecx,4),%eax
	cmpl	$16,%ecx
	jge	.L51
.L50:
	movl	$0,(%eax)
	leal	4(%eax),%eax
	cmpl	-272(%ebp),%eax
	jl	.L50
.L51:
	movl	$_tanaka,-312(%ebp)
	movl	$_suzuki,-316(%ebp)
	movl	$_tanaka+12,-308(%ebp)
	call	_ProcSemi
	leal	-132(%ebp),%eax
	pushl	%eax
	leal	-68(%ebp),%eax
	pushl	%eax
	call	_Funcx
	leal	8(%esp),%esp
	pushl	_z
	pushl	_y
	pushl	%eax
	pushl	$_string.55
	call	_printf
	leal	16(%esp),%esp
	pushl	$_string.57
	pushl	-312(%ebp)
	call	_strcpy
	leal	8(%esp),%esp
	pushl	$_string.59
	pushl	-316(%ebp)
	call	_strcpy
	leal	8(%esp),%esp
	movl	-308(%ebp),%eax
	movl	$1975,(%eax)
	movl	-308(%ebp),%eax
	leal	4(%eax),%eax
	movl	%eax,-276(%ebp)
	movl	-276(%ebp),%eax
	movl	$1,(%eax)
	movl	-308(%ebp),%eax
	leal	8(%eax),%edi
	movl	$10,(%edi)
	movl	$_tanaka+24,%edx
	movl	$1993,(%edx)
	leal	4(%edx),%ecx
	movl	$3,(%ecx)
	leal	8(%edx),%eax
	movl	$20,(%eax)
	movl	$_suzuki+12,-300(%ebp)
	movl	-300(%ebp),%ebx
	movl	$1975,(%ebx)
	movl	-300(%ebp),%ebx
	leal	4(%ebx),%ebx
	movl	%ebx,-304(%ebp)
	movl	-304(%ebp),%ebx
	movl	$1,(%ebx)
	movl	-300(%ebp),%ebx
	leal	8(%ebx),%ebx
	movl	%ebx,-296(%ebp)
	movl	-296(%ebp),%ebx
	movl	$10,(%ebx)
	movl	$_suzuki+24,%ebx
	movl	$1993,(%ebx)
	leal	4(%ebx),%esi
	movl	%esi,-268(%ebp)
	movl	-268(%ebp),%esi
	movl	$3,(%esi)
	leal	8(%ebx),%esi
	movl	$20,(%esi)
	pushl	(%eax)
	pushl	(%ecx)
	pushl	(%edx)
	pushl	(%edi)
	movl	-276(%ebp),%eax
	pushl	(%eax)
	movl	-308(%ebp),%eax
	pushl	(%eax)
	pushl	-312(%ebp)
	pushl	$_string.61
	call	_printf
	leal	32(%esp),%esp
	pushl	(%esi)
	movl	-268(%ebp),%eax
	pushl	(%eax)
	pushl	(%ebx)
	movl	-296(%ebp),%eax
	pushl	(%eax)
	movl	-304(%ebp),%eax
	pushl	(%eax)
	movl	-300(%ebp),%eax
	pushl	(%eax)
	pushl	-316(%ebp)
	pushl	$_string.61
	call	_printf
	leal	32(%esp),%esp
	pushl	$2176
	pushl	$_string.64
	call	_printf
	leal	8(%esp),%esp
	pushl	$5
	pushl	$4
	pushl	$3
	pushl	$_string.66
	call	_printf
	leal	16(%esp),%esp
.L76:
	popl	%edi
	popl	%esi
	popl	%ebx
	leave
	ret

	.data
	.align	4
	.global	_x
_x:
	.long	1
	.align	4
	.global	_y
_y:
	.long	2
	.align	4
	.global	_z
_z:
	.long	3
	.comm	_rgbp,4
	.comm	_rgbbuf,4
	.comm	_tanaka,36
	.comm	_yamada,36
	.comm	_suzuki,36
