 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */

	.text
	.align	4
	.global	_f0
_f0:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	movl	8(%ebp),%ecx
	movl	$_sa+8,%eax
	subl	%ecx,%eax
	movl	$4,%ebx
	cdq
	idivl	%ebx
	pushl	%eax
	movl	$_sa,%eax
	subl	%ecx,%eax
	movl	$4,%ebx
	cdq
	idivl	%ebx
	pushl	%eax
	movl	%ecx,%eax
	subl	$_sa+8,%eax
	movl	$4,%ebx
	cdq
	idivl	%ebx
	pushl	%eax
	movl	%ecx,%eax
	subl	$_sa,%eax
	movl	$4,%ecx
	cdq
	idivl	%ecx
	pushl	%eax
	pushl	$_s4
	call	_printf
	leal	20(%esp),%esp
.L3:
	popl	%ebx
	leave
	ret


	.align	4
	.global	_g0
_g0:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	movl	8(%ebp),%ecx
	movl	$_aa+12,%eax
	subl	%ecx,%eax
	movl	$6,%ebx
	cdq
	idivl	%ebx
	pushl	%eax
	movl	$_aa,%eax
	subl	%ecx,%eax
	movl	$6,%ebx
	cdq
	idivl	%ebx
	pushl	%eax
	movl	%ecx,%eax
	subl	$_aa+12,%eax
	movl	$6,%ebx
	cdq
	idivl	%ebx
	pushl	%eax
	movl	%ecx,%eax
	subl	$_aa,%eax
	movl	$6,%ecx
	cdq
	idivl	%ecx
	pushl	%eax
	pushl	$_s4
	call	_printf
	leal	20(%esp),%esp
.L6:
	popl	%ebx
	leave
	ret


	.align	4
	.global	_op0
_op0:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%eax
	movl	12(%ebp),%ecx
	subl	%ecx,%eax
	movl	$4,%ecx
	cdq
	idivl	%ecx
	leave
	ret


	.align	4
	.global	_op1
_op1:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%eax
	movl	12(%ebp),%ecx
	subl	%ecx,%eax
	movl	$6,%ecx
	cdq
	idivl	%ecx
	leave
	ret


	.align	4
	.global	_f2
_f2:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$4,%esp
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	movl	8(%ebp),%edi
	pushl	$_sa
	pushl	%edi
	call	_op0
	movl	%eax,-4(%ebp)
	leal	8(%esp),%esp
	pushl	$_sa+8
	pushl	%edi
	call	_op0
	movl	%eax,%esi
	leal	8(%esp),%esp
	pushl	%edi
	pushl	$_sa
	call	_op0
	movl	%eax,%ebx
	leal	8(%esp),%esp
	pushl	%edi
	pushl	$_sa+8
	call	_op0
	leal	8(%esp),%esp
	pushl	%eax
	pushl	%ebx
	pushl	%esi
	pushl	-4(%ebp)
	pushl	$_s4
	call	_printf
	leal	20(%esp),%esp
.L15:
	popl	%edi
	popl	%esi
	popl	%ebx
	leave
	ret


	.align	4
	.global	_g2
_g2:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$4,%esp
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	movl	8(%ebp),%edi
	pushl	$_aa
	pushl	%edi
	call	_op1
	movl	%eax,-4(%ebp)
	leal	8(%esp),%esp
	pushl	$_aa+12
	pushl	%edi
	call	_op1
	movl	%eax,%esi
	leal	8(%esp),%esp
	pushl	%edi
	pushl	$_aa
	call	_op1
	movl	%eax,%ebx
	leal	8(%esp),%esp
	pushl	%edi
	pushl	$_aa+12
	call	_op1
	leal	8(%esp),%esp
	pushl	%eax
	pushl	%ebx
	pushl	%esi
	pushl	-4(%ebp)
	pushl	$_s4
	call	_printf
	leal	20(%esp),%esp
.L18:
	popl	%edi
	popl	%esi
	popl	%ebx
	leave
	ret


	.align	4
	.global	_main0g
_main0g:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$16,%esp
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	movl	$_sa+16,%eax
	subl	$_sa,%eax
	movl	$4,%ecx
	cdq
	idivl	%ecx
	movl	%eax,-16(%ebp)
	movl	$_sa+16,%eax
	subl	$_sa+8,%eax
	movl	$4,%ecx
	cdq
	idivl	%ecx
	movl	%eax,-12(%ebp)
	movl	$_sa,%eax
	subl	$_sa+16,%eax
	movl	$4,%ecx
	cdq
	idivl	%ecx
	movl	%eax,-8(%ebp)
	movl	$_sa+8,%eax
	subl	$_sa+16,%eax
	movl	$4,%ecx
	cdq
	idivl	%ecx
	movl	%eax,%ecx
	movl	$_aa+24,%eax
	subl	$_aa,%eax
	movl	$6,%ebx
	cdq
	idivl	%ebx
	movl	%eax,-4(%ebp)
	movl	$_aa+24,%eax
	subl	$_aa+12,%eax
	movl	$6,%ebx
	cdq
	idivl	%ebx
	movl	%eax,%ebx
	movl	$_aa,%eax
	subl	$_aa+24,%eax
	movl	$6,%esi
	cdq
	idivl	%esi
	movl	%eax,%esi
	movl	$_aa+12,%eax
	subl	$_aa+24,%eax
	movl	$6,%edi
	cdq
	idivl	%edi
	movl	%eax,%edi
	pushl	%ecx
	pushl	-8(%ebp)
	pushl	-12(%ebp)
	pushl	-16(%ebp)
	pushl	$_s4
	call	_printf
	leal	20(%esp),%esp
	pushl	%edi
	pushl	%esi
	pushl	%ebx
	pushl	-4(%ebp)
	pushl	$_s4
	call	_printf
	leal	20(%esp),%esp
.L21:
	popl	%edi
	popl	%esi
	popl	%ebx
	leave
	ret


	.align	4
	.global	_main0l
_main0l:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$56,%esp
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	movw	$0,-16(%ebp)
	movw	$1,-14(%ebp)
	movw	$2,-12(%ebp)
	movw	$3,-10(%ebp)
	movw	$4,-8(%ebp)
	movw	$5,-6(%ebp)
	movw	$6,-4(%ebp)
	movw	$7,-2(%ebp)
	movw	$0,-40(%ebp)
	movw	$1,-38(%ebp)
	movw	$2,-36(%ebp)
	movw	$3,-34(%ebp)
	movw	$4,-32(%ebp)
	movw	$5,-30(%ebp)
	movw	$6,-28(%ebp)
	movw	$7,-26(%ebp)
	movw	$8,-24(%ebp)
	movw	$9,-22(%ebp)
	movw	$10,-20(%ebp)
	movw	$11,-18(%ebp)
	leal	-16(%ebp),%ecx
	movl	%ebp,%eax
	subl	%ecx,%eax
	movl	$4,%ecx
	cdq
	idivl	%ecx
	movl	%eax,-56(%ebp)
	leal	-8(%ebp),%ecx
	movl	%ebp,%eax
	subl	%ecx,%eax
	movl	$4,%ecx
	cdq
	idivl	%ecx
	movl	%eax,-52(%ebp)
	leal	-16(%ebp),%eax
	subl	%ebp,%eax
	movl	$4,%ecx
	cdq
	idivl	%ecx
	movl	%eax,-48(%ebp)
	leal	-8(%ebp),%eax
	subl	%ebp,%eax
	movl	$4,%ecx
	cdq
	idivl	%ecx
	movl	%eax,%ecx
	leal	-16(%ebp),%eax
	leal	-40(%ebp),%edx
	subl	%edx,%eax
	movl	$6,%ebx
	cdq
	idivl	%ebx
	movl	%eax,-44(%ebp)
	leal	-16(%ebp),%eax
	leal	-28(%ebp),%edx
	subl	%edx,%eax
	movl	$6,%ebx
	cdq
	idivl	%ebx
	movl	%eax,%ebx
	leal	-40(%ebp),%eax
	leal	-16(%ebp),%edx
	subl	%edx,%eax
	movl	$6,%esi
	cdq
	idivl	%esi
	movl	%eax,%esi
	leal	-28(%ebp),%eax
	leal	-16(%ebp),%edx
	subl	%edx,%eax
	movl	$6,%edi
	cdq
	idivl	%edi
	movl	%eax,%edi
	pushl	%ecx
	pushl	-48(%ebp)
	pushl	-52(%ebp)
	pushl	-56(%ebp)
	pushl	$_s4
	call	_printf
	leal	20(%esp),%esp
	pushl	%edi
	pushl	%esi
	pushl	%ebx
	pushl	-44(%ebp)
	pushl	$_s4
	call	_printf
	leal	20(%esp),%esp
.L24:
	popl	%edi
	popl	%esi
	popl	%ebx
	leave
	ret


	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	call	_main0g
	call	_main0l
	movl	$_sa+16,%ebx
	movl	$_sa+8,%eax
	subl	%ebx,%eax
	movl	$4,%ecx
	cdq
	idivl	%ecx
	pushl	%eax
	movl	$_sa,%eax
	subl	%ebx,%eax
	movl	$4,%ecx
	cdq
	idivl	%ecx
	pushl	%eax
	movl	%ebx,%eax
	subl	$_sa+8,%eax
	movl	$4,%ecx
	cdq
	idivl	%ecx
	pushl	%eax
	movl	%ebx,%eax
	subl	$_sa,%eax
	movl	$4,%ecx
	cdq
	idivl	%ecx
	pushl	%eax
	pushl	$_s4
	call	_printf
	leal	20(%esp),%esp
	movl	$_sa+8,%eax
	subl	%ebx,%eax
	movl	$4,%ecx
	cdq
	idivl	%ecx
	pushl	%eax
	movl	$_sa,%eax
	subl	%ebx,%eax
	movl	$4,%ecx
	cdq
	idivl	%ecx
	pushl	%eax
	movl	%ebx,%eax
	subl	$_sa+8,%eax
	movl	$4,%ecx
	cdq
	idivl	%ecx
	pushl	%eax
	movl	%ebx,%eax
	subl	$_sa,%eax
	movl	$4,%ecx
	cdq
	idivl	%ecx
	pushl	%eax
	pushl	$_s4
	call	_printf
	leal	20(%esp),%esp
	movl	$_aa+24,%ebx
	movl	$_aa+12,%eax
	subl	%ebx,%eax
	movl	$6,%ecx
	cdq
	idivl	%ecx
	pushl	%eax
	movl	$_aa,%eax
	subl	%ebx,%eax
	movl	$6,%ecx
	cdq
	idivl	%ecx
	pushl	%eax
	movl	%ebx,%eax
	subl	$_aa+12,%eax
	movl	$6,%ecx
	cdq
	idivl	%ecx
	pushl	%eax
	movl	%ebx,%eax
	subl	$_aa,%eax
	movl	$6,%ecx
	cdq
	idivl	%ecx
	pushl	%eax
	pushl	$_s4
	call	_printf
	leal	20(%esp),%esp
	movl	$_aa+12,%eax
	subl	%ebx,%eax
	movl	$6,%ecx
	cdq
	idivl	%ecx
	pushl	%eax
	movl	$_aa,%eax
	subl	%ebx,%eax
	movl	$6,%ecx
	cdq
	idivl	%ecx
	pushl	%eax
	movl	%ebx,%eax
	subl	$_aa+12,%eax
	movl	$6,%ecx
	cdq
	idivl	%ecx
	pushl	%eax
	movl	%ebx,%eax
	subl	$_aa,%eax
	movl	$6,%ecx
	cdq
	idivl	%ecx
	pushl	%eax
	pushl	$_s4
	call	_printf
	leal	20(%esp),%esp
	pushl	$_sa+16
	call	_f0
	leal	4(%esp),%esp
	pushl	$_aa+24
	call	_g0
	leal	4(%esp),%esp
	pushl	$_sa+16
	call	_f2
	leal	4(%esp),%esp
	pushl	$_aa+24
	call	_g2
	leal	4(%esp),%esp
	movl	$0,%eax
	popl	%ebx
	leave
	ret

	.data
	.align	1
	.global	_s4
_s4:
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
	.align	2
	.global	_sa
_sa:
	.short	0
	.short	1
	.short	2
	.short	3
	.short	4
	.short	5
	.short	6
	.short	7
	.align	2
	.global	_aa
_aa:
	.short	0
	.short	1
	.short	2
	.short	3
	.short	4
	.short	5
	.short	6
	.short	7
	.short	8
	.short	9
	.short	10
	.short	11
