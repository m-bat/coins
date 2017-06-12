 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */

	.text
	.align	4
	.global	_fsc
_fsc:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$28,%esp
	flds	8(%ebp)
	fstps	-4(%ebp)
	fldl	12(%ebp)
	fstpl	-12(%ebp)
	fldl	20(%ebp)
	fstpl	-20(%ebp)
	fldl	-20(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movb	-28(%ebp),%al
	fldcw	-22(%ebp)
	movsbl	%al,%eax
	pushl	%eax
	fldl	-12(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movb	-28(%ebp),%al
	fldcw	-22(%ebp)
	movsbl	%al,%eax
	pushl	%eax
	flds	-4(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movb	-28(%ebp),%al
	fldcw	-22(%ebp)
	movsbl	%al,%eax
	pushl	%eax
	pushl	$_s1
	call	_printf
	leal	16(%esp),%esp
.L3:
	leave
	ret


	.align	4
	.global	_fuc
_fuc:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$28,%esp
	flds	8(%ebp)
	fstps	-4(%ebp)
	fldl	12(%ebp)
	fstpl	-12(%ebp)
	fldl	20(%ebp)
	fstpl	-20(%ebp)
	flds	-4(%ebp)
	fcomps	.LC1
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L29
.L28:
	flds	-4(%ebp)
	fsubs	.LC1
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movb	-28(%ebp),%cl
	fldcw	-22(%ebp)
	jmp	.L30
.L29:
	flds	-4(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movb	-28(%ebp),%cl
	fldcw	-22(%ebp)
.L30:
	fldl	-12(%ebp)
	fcompl	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L32
.L31:
	fldl	-12(%ebp)
	fsubl	.LC2
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movb	-28(%ebp),%ch
	fldcw	-22(%ebp)
	jmp	.L33
.L32:
	fldl	-12(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movb	-28(%ebp),%ch
	fldcw	-22(%ebp)
.L33:
	fldl	-20(%ebp)
	fcompl	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L35
.L34:
	fldl	-20(%ebp)
	fsubl	.LC2
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movb	-28(%ebp),%al
	fldcw	-22(%ebp)
	jmp	.L36
.L35:
	fldl	-20(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movb	-28(%ebp),%al
	fldcw	-22(%ebp)
.L36:
	movzbl	%al,%eax
	pushl	%eax
	movzbl	%ch,%eax
	pushl	%eax
	movzbl	%cl,%eax
	pushl	%eax
	pushl	$_s1
	call	_printf
	leal	16(%esp),%esp
.L6:
	leave
	ret


	.align	4
	.global	_fs
_fs:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$28,%esp
	flds	8(%ebp)
	fstps	-4(%ebp)
	fldl	12(%ebp)
	fstpl	-12(%ebp)
	fldl	20(%ebp)
	fstpl	-20(%ebp)
	fldl	-20(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movw	-28(%ebp),%ax
	fldcw	-22(%ebp)
	movswl	%ax,%eax
	pushl	%eax
	fldl	-12(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movw	-28(%ebp),%ax
	fldcw	-22(%ebp)
	movswl	%ax,%eax
	pushl	%eax
	flds	-4(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movw	-28(%ebp),%ax
	fldcw	-22(%ebp)
	movswl	%ax,%eax
	pushl	%eax
	pushl	$_s1
	call	_printf
	leal	16(%esp),%esp
.L9:
	leave
	ret


	.align	4
	.global	_fus
_fus:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$28,%esp
	flds	8(%ebp)
	fstps	-4(%ebp)
	fldl	12(%ebp)
	fstpl	-12(%ebp)
	fldl	20(%ebp)
	fstpl	-20(%ebp)
	flds	-4(%ebp)
	fcomps	.LC1
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L38
.L37:
	flds	-4(%ebp)
	fsubs	.LC1
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%dx
	orw	$3072,%dx
	movw	%dx,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movw	-28(%ebp),%dx
	fldcw	-22(%ebp)
	jmp	.L39
.L38:
	flds	-4(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%dx
	orw	$3072,%dx
	movw	%dx,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movw	-28(%ebp),%dx
	fldcw	-22(%ebp)
.L39:
	fldl	-12(%ebp)
	fcompl	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L41
.L40:
	fldl	-12(%ebp)
	fsubl	.LC2
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%cx
	orw	$3072,%cx
	movw	%cx,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movw	-28(%ebp),%cx
	fldcw	-22(%ebp)
	jmp	.L42
.L41:
	fldl	-12(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%cx
	orw	$3072,%cx
	movw	%cx,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movw	-28(%ebp),%cx
	fldcw	-22(%ebp)
.L42:
	fldl	-20(%ebp)
	fcompl	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L44
.L43:
	fldl	-20(%ebp)
	fsubl	.LC2
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movw	-28(%ebp),%ax
	fldcw	-22(%ebp)
	jmp	.L45
.L44:
	fldl	-20(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movw	-28(%ebp),%ax
	fldcw	-22(%ebp)
.L45:
	movzwl	%ax,%eax
	pushl	%eax
	movzwl	%cx,%eax
	pushl	%eax
	movzwl	%dx,%eax
	pushl	%eax
	pushl	$_s1
	call	_printf
	leal	16(%esp),%esp
.L12:
	leave
	ret


	.align	4
	.global	_fi
_fi:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$28,%esp
	flds	8(%ebp)
	fstps	-4(%ebp)
	fldl	12(%ebp)
	fstpl	-12(%ebp)
	fldl	20(%ebp)
	fstpl	-20(%ebp)
	fldl	-20(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%eax
	fldcw	-22(%ebp)
	pushl	%eax
	fldl	-12(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%eax
	fldcw	-22(%ebp)
	pushl	%eax
	flds	-4(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%eax
	fldcw	-22(%ebp)
	pushl	%eax
	pushl	$_s1
	call	_printf
	leal	16(%esp),%esp
.L15:
	leave
	ret


	.align	4
	.global	_fui
_fui:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$28,%esp
	flds	8(%ebp)
	fstps	-4(%ebp)
	fldl	12(%ebp)
	fstpl	-12(%ebp)
	fldl	20(%ebp)
	fstpl	-20(%ebp)
	flds	-4(%ebp)
	fcomps	.LC1
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L47
.L46:
	flds	-4(%ebp)
	fsubs	.LC1
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%eax
	fldcw	-22(%ebp)
	leal	-2147483648(%eax),%edx
	jmp	.L48
.L47:
	flds	-4(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%dx
	orw	$3072,%dx
	movw	%dx,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%edx
	fldcw	-22(%ebp)
.L48:
	fldl	-12(%ebp)
	fcompl	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L50
.L49:
	fldl	-12(%ebp)
	fsubl	.LC2
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%eax
	fldcw	-22(%ebp)
	leal	-2147483648(%eax),%ecx
	jmp	.L51
.L50:
	fldl	-12(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%cx
	orw	$3072,%cx
	movw	%cx,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%ecx
	fldcw	-22(%ebp)
.L51:
	fldl	-20(%ebp)
	fcompl	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L53
.L52:
	fldl	-20(%ebp)
	fsubl	.LC2
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%eax
	fldcw	-22(%ebp)
	leal	-2147483648(%eax),%eax
	jmp	.L54
.L53:
	fldl	-20(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%eax
	fldcw	-22(%ebp)
.L54:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s1
	call	_printf
	leal	16(%esp),%esp
.L18:
	leave
	ret


	.align	4
	.global	_fl
_fl:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$28,%esp
	flds	8(%ebp)
	fstps	-4(%ebp)
	fldl	12(%ebp)
	fstpl	-12(%ebp)
	fldl	20(%ebp)
	fstpl	-20(%ebp)
	fldl	-20(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%eax
	fldcw	-22(%ebp)
	pushl	%eax
	fldl	-12(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%eax
	fldcw	-22(%ebp)
	pushl	%eax
	flds	-4(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%eax
	fldcw	-22(%ebp)
	pushl	%eax
	pushl	$_s2
	call	_printf
	leal	16(%esp),%esp
.L21:
	leave
	ret


	.align	4
	.global	_ful
_ful:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$28,%esp
	flds	8(%ebp)
	fstps	-4(%ebp)
	fldl	12(%ebp)
	fstpl	-12(%ebp)
	fldl	20(%ebp)
	fstpl	-20(%ebp)
	flds	-4(%ebp)
	fcomps	.LC1
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L56
.L55:
	flds	-4(%ebp)
	fsubs	.LC1
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%eax
	fldcw	-22(%ebp)
	leal	-2147483648(%eax),%edx
	jmp	.L57
.L56:
	flds	-4(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%dx
	orw	$3072,%dx
	movw	%dx,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%edx
	fldcw	-22(%ebp)
.L57:
	fldl	-12(%ebp)
	fcompl	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L59
.L58:
	fldl	-12(%ebp)
	fsubl	.LC2
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%eax
	fldcw	-22(%ebp)
	leal	-2147483648(%eax),%ecx
	jmp	.L60
.L59:
	fldl	-12(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%cx
	orw	$3072,%cx
	movw	%cx,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%ecx
	fldcw	-22(%ebp)
.L60:
	fldl	-20(%ebp)
	fcompl	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L62
.L61:
	fldl	-20(%ebp)
	fsubl	.LC2
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%eax
	fldcw	-22(%ebp)
	leal	-2147483648(%eax),%eax
	jmp	.L63
.L62:
	fldl	-20(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%eax
	fldcw	-22(%ebp)
.L63:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s2
	call	_printf
	leal	16(%esp),%esp
.L24:
	leave
	ret


	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$28,%esp
	movsbl	_scld0,%eax
	pushl	%eax
	movsbl	_scd0,%eax
	pushl	%eax
	movsbl	_scf0,%eax
	pushl	%eax
	pushl	$_s1
	call	_printf
	leal	16(%esp),%esp
	flds	.LC3
	fstps	-4(%ebp)
	fldl	.LC4
	fstpl	-12(%ebp)
	fldl	.LC4
	fstpl	-20(%ebp)
	pushl	$127
	pushl	$127
	pushl	$127
	pushl	$_s1
	call	_printf
	leal	16(%esp),%esp
	pushl	$127
	pushl	$127
	pushl	$127
	pushl	$_s1
	call	_printf
	leal	16(%esp),%esp
	pushl	.LC4+4
	pushl	.LC4
	pushl	.LC4+4
	pushl	.LC4
	pushl	.LC3
	call	_fsc
	leal	20(%esp),%esp
	movzbl	_ucld0,%eax
	pushl	%eax
	movzbl	_ucd0,%eax
	pushl	%eax
	movzbl	_ucf0,%eax
	pushl	%eax
	pushl	$_s1
	call	_printf
	leal	16(%esp),%esp
	flds	.LC5
	fstps	-4(%ebp)
	fldl	.LC6
	fstpl	-12(%ebp)
	fldl	.LC6
	fstpl	-20(%ebp)
	flds	-4(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movb	-28(%ebp),%cl
	fldcw	-22(%ebp)
	fldl	-12(%ebp)
	fcompl	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L68
.L67:
	fldl	-12(%ebp)
	fsubl	.LC2
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movb	-28(%ebp),%ch
	fldcw	-22(%ebp)
	jmp	.L69
.L68:
	fldl	-12(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movb	-28(%ebp),%ch
	fldcw	-22(%ebp)
.L69:
	fldl	-20(%ebp)
	fcompl	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L71
.L70:
	fldl	-20(%ebp)
	fsubl	.LC2
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movb	-28(%ebp),%al
	fldcw	-22(%ebp)
	jmp	.L72
.L71:
	fldl	-20(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movb	-28(%ebp),%al
	fldcw	-22(%ebp)
.L72:
	movzbl	%al,%eax
	pushl	%eax
	movzbl	%ch,%eax
	pushl	%eax
	movzbl	%cl,%eax
	pushl	%eax
	pushl	$_s1
	call	_printf
	leal	16(%esp),%esp
	flds	-4(%ebp)
	fcomps	.LC1
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L74
.L73:
	flds	-4(%ebp)
	fsubs	.LC1
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movb	-28(%ebp),%cl
	fldcw	-22(%ebp)
	jmp	.L75
.L74:
	flds	-4(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movb	-28(%ebp),%cl
	fldcw	-22(%ebp)
.L75:
	fldl	-12(%ebp)
	fcompl	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L77
.L76:
	fldl	-12(%ebp)
	fsubl	.LC2
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movb	-28(%ebp),%ch
	fldcw	-22(%ebp)
	jmp	.L78
.L77:
	fldl	-12(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movb	-28(%ebp),%ch
	fldcw	-22(%ebp)
.L78:
	fldl	-20(%ebp)
	fcompl	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L80
.L79:
	fldl	-20(%ebp)
	fsubl	.LC2
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movb	-28(%ebp),%al
	fldcw	-22(%ebp)
	jmp	.L81
.L80:
	fldl	-20(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movb	-28(%ebp),%al
	fldcw	-22(%ebp)
.L81:
	movzbl	%al,%eax
	pushl	%eax
	movzbl	%ch,%eax
	pushl	%eax
	movzbl	%cl,%eax
	pushl	%eax
	pushl	$_s1
	call	_printf
	leal	16(%esp),%esp
	pushl	.LC6+4
	pushl	.LC6
	pushl	.LC6+4
	pushl	.LC6
	pushl	.LC5
	call	_fuc
	leal	20(%esp),%esp
	movswl	_sld0,%eax
	pushl	%eax
	movswl	_sd0,%eax
	pushl	%eax
	movswl	_sf0,%eax
	pushl	%eax
	pushl	$_s1
	call	_printf
	leal	16(%esp),%esp
	flds	.LC7
	fstps	-4(%ebp)
	fldl	.LC8
	fstpl	-12(%ebp)
	fldl	.LC8
	fstpl	-20(%ebp)
	pushl	$32767
	pushl	$32767
	pushl	$32767
	pushl	$_s1
	call	_printf
	leal	16(%esp),%esp
	pushl	$32767
	pushl	$32767
	pushl	$32767
	pushl	$_s1
	call	_printf
	leal	16(%esp),%esp
	pushl	.LC8+4
	pushl	.LC8
	pushl	.LC8+4
	pushl	.LC8
	pushl	.LC7
	call	_fs
	leal	20(%esp),%esp
	movzwl	_usld0,%eax
	pushl	%eax
	movzwl	_usd0,%eax
	pushl	%eax
	movzwl	_usf0,%eax
	pushl	%eax
	pushl	$_s1
	call	_printf
	leal	16(%esp),%esp
	flds	.LC9
	fstps	-4(%ebp)
	fldl	.LC10
	fstpl	-12(%ebp)
	fldl	.LC10
	fstpl	-20(%ebp)
	flds	-4(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%dx
	orw	$3072,%dx
	movw	%dx,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movw	-28(%ebp),%dx
	fldcw	-22(%ebp)
	fldl	-12(%ebp)
	fcompl	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L86
.L85:
	fldl	-12(%ebp)
	fsubl	.LC2
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%cx
	orw	$3072,%cx
	movw	%cx,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movw	-28(%ebp),%cx
	fldcw	-22(%ebp)
	jmp	.L87
.L86:
	fldl	-12(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%cx
	orw	$3072,%cx
	movw	%cx,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movw	-28(%ebp),%cx
	fldcw	-22(%ebp)
.L87:
	fldl	-20(%ebp)
	fcompl	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L89
.L88:
	fldl	-20(%ebp)
	fsubl	.LC2
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movw	-28(%ebp),%ax
	fldcw	-22(%ebp)
	jmp	.L90
.L89:
	fldl	-20(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movw	-28(%ebp),%ax
	fldcw	-22(%ebp)
.L90:
	movzwl	%ax,%eax
	pushl	%eax
	movzwl	%cx,%eax
	pushl	%eax
	movzwl	%dx,%eax
	pushl	%eax
	pushl	$_s1
	call	_printf
	leal	16(%esp),%esp
	flds	-4(%ebp)
	fcomps	.LC1
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L92
.L91:
	flds	-4(%ebp)
	fsubs	.LC1
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%dx
	orw	$3072,%dx
	movw	%dx,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movw	-28(%ebp),%dx
	fldcw	-22(%ebp)
	jmp	.L93
.L92:
	flds	-4(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%dx
	orw	$3072,%dx
	movw	%dx,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movw	-28(%ebp),%dx
	fldcw	-22(%ebp)
.L93:
	fldl	-12(%ebp)
	fcompl	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L95
.L94:
	fldl	-12(%ebp)
	fsubl	.LC2
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%cx
	orw	$3072,%cx
	movw	%cx,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movw	-28(%ebp),%cx
	fldcw	-22(%ebp)
	jmp	.L96
.L95:
	fldl	-12(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%cx
	orw	$3072,%cx
	movw	%cx,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movw	-28(%ebp),%cx
	fldcw	-22(%ebp)
.L96:
	fldl	-20(%ebp)
	fcompl	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L98
.L97:
	fldl	-20(%ebp)
	fsubl	.LC2
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movw	-28(%ebp),%ax
	fldcw	-22(%ebp)
	jmp	.L99
.L98:
	fldl	-20(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movw	-28(%ebp),%ax
	fldcw	-22(%ebp)
.L99:
	movzwl	%ax,%eax
	pushl	%eax
	movzwl	%cx,%eax
	pushl	%eax
	movzwl	%dx,%eax
	pushl	%eax
	pushl	$_s1
	call	_printf
	leal	16(%esp),%esp
	pushl	.LC10+4
	pushl	.LC10
	pushl	.LC10+4
	pushl	.LC10
	pushl	.LC9
	call	_fus
	leal	20(%esp),%esp
	pushl	_ild0
	pushl	_id0
	pushl	_if0
	pushl	$_s1
	call	_printf
	leal	16(%esp),%esp
	flds	.LC11
	fstps	-4(%ebp)
	fldl	.LC12
	fstpl	-12(%ebp)
	fldl	.LC12
	fstpl	-20(%ebp)
	pushl	$2147483647
	pushl	$2147483647
	pushl	$2147483520
	pushl	$_s1
	call	_printf
	leal	16(%esp),%esp
	pushl	$2147483647
	pushl	$2147483647
	pushl	$2147483520
	pushl	$_s1
	call	_printf
	leal	16(%esp),%esp
	pushl	.LC12+4
	pushl	.LC12
	pushl	.LC12+4
	pushl	.LC12
	pushl	.LC11
	call	_fi
	leal	20(%esp),%esp
	pushl	_uild0
	pushl	_uid0
	pushl	_uif0
	pushl	$_s1
	call	_printf
	leal	16(%esp),%esp
	flds	.LC13
	fstps	-4(%ebp)
	fldl	.LC14
	fstpl	-12(%ebp)
	fldl	.LC14
	fstpl	-20(%ebp)
	flds	-4(%ebp)
	fsubs	.LC1
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%eax
	fldcw	-22(%ebp)
	leal	-2147483648(%eax),%edx
	fldl	-12(%ebp)
	fcompl	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L104
.L103:
	fldl	-12(%ebp)
	fsubl	.LC2
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%eax
	fldcw	-22(%ebp)
	leal	-2147483648(%eax),%ecx
	jmp	.L105
.L104:
	fldl	-12(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%cx
	orw	$3072,%cx
	movw	%cx,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%ecx
	fldcw	-22(%ebp)
.L105:
	fldl	-20(%ebp)
	fcompl	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L107
.L106:
	fldl	-20(%ebp)
	fsubl	.LC2
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%eax
	fldcw	-22(%ebp)
	leal	-2147483648(%eax),%eax
	jmp	.L108
.L107:
	fldl	-20(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%eax
	fldcw	-22(%ebp)
.L108:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s1
	call	_printf
	leal	16(%esp),%esp
	flds	-4(%ebp)
	fcomps	.LC1
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L110
.L109:
	flds	-4(%ebp)
	fsubs	.LC1
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%eax
	fldcw	-22(%ebp)
	leal	-2147483648(%eax),%edx
	jmp	.L111
.L110:
	flds	-4(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%dx
	orw	$3072,%dx
	movw	%dx,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%edx
	fldcw	-22(%ebp)
.L111:
	fldl	-12(%ebp)
	fcompl	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L113
.L112:
	fldl	-12(%ebp)
	fsubl	.LC2
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%eax
	fldcw	-22(%ebp)
	leal	-2147483648(%eax),%ecx
	jmp	.L114
.L113:
	fldl	-12(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%cx
	orw	$3072,%cx
	movw	%cx,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%ecx
	fldcw	-22(%ebp)
.L114:
	fldl	-20(%ebp)
	fcompl	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L116
.L115:
	fldl	-20(%ebp)
	fsubl	.LC2
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%eax
	fldcw	-22(%ebp)
	leal	-2147483648(%eax),%eax
	jmp	.L117
.L116:
	fldl	-20(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%eax
	fldcw	-22(%ebp)
.L117:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s1
	call	_printf
	leal	16(%esp),%esp
	pushl	.LC14+4
	pushl	.LC14
	pushl	.LC14+4
	pushl	.LC14
	pushl	.LC13
	call	_fui
	leal	20(%esp),%esp
	pushl	_lld0
	pushl	_ld0
	pushl	_lf0
	pushl	$_s1
	call	_printf
	leal	16(%esp),%esp
	flds	.LC11
	fstps	-4(%ebp)
	fldl	.LC12
	fstpl	-12(%ebp)
	fldl	.LC12
	fstpl	-20(%ebp)
	pushl	$2147483647
	pushl	$2147483647
	pushl	$2147483520
	pushl	$_s2
	call	_printf
	leal	16(%esp),%esp
	pushl	$2147483647
	pushl	$2147483647
	pushl	$2147483520
	pushl	$_s2
	call	_printf
	leal	16(%esp),%esp
	pushl	.LC12+4
	pushl	.LC12
	pushl	.LC12+4
	pushl	.LC12
	pushl	.LC11
	call	_fl
	leal	20(%esp),%esp
	pushl	_ulld0
	pushl	_uld0
	pushl	_ulf0
	pushl	$_s1
	call	_printf
	leal	16(%esp),%esp
	flds	.LC13
	fstps	-4(%ebp)
	fldl	.LC14
	fstpl	-12(%ebp)
	fldl	.LC14
	fstpl	-20(%ebp)
	flds	-4(%ebp)
	fsubs	.LC1
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%eax
	fldcw	-22(%ebp)
	leal	-2147483648(%eax),%edx
	fldl	-12(%ebp)
	fcompl	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L122
.L121:
	fldl	-12(%ebp)
	fsubl	.LC2
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%eax
	fldcw	-22(%ebp)
	leal	-2147483648(%eax),%ecx
	jmp	.L123
.L122:
	fldl	-12(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%cx
	orw	$3072,%cx
	movw	%cx,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%ecx
	fldcw	-22(%ebp)
.L123:
	fldl	-20(%ebp)
	fcompl	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L125
.L124:
	fldl	-20(%ebp)
	fsubl	.LC2
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%eax
	fldcw	-22(%ebp)
	leal	-2147483648(%eax),%eax
	jmp	.L126
.L125:
	fldl	-20(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%eax
	fldcw	-22(%ebp)
.L126:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s2
	call	_printf
	leal	16(%esp),%esp
	flds	-4(%ebp)
	fcomps	.LC1
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L128
.L127:
	flds	-4(%ebp)
	fsubs	.LC1
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%eax
	fldcw	-22(%ebp)
	leal	-2147483648(%eax),%edx
	jmp	.L129
.L128:
	flds	-4(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%dx
	orw	$3072,%dx
	movw	%dx,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%edx
	fldcw	-22(%ebp)
.L129:
	fldl	-12(%ebp)
	fcompl	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L131
.L130:
	fldl	-12(%ebp)
	fsubl	.LC2
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%eax
	fldcw	-22(%ebp)
	leal	-2147483648(%eax),%ecx
	jmp	.L132
.L131:
	fldl	-12(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%cx
	orw	$3072,%cx
	movw	%cx,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%ecx
	fldcw	-22(%ebp)
.L132:
	fldl	-20(%ebp)
	fcompl	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L134
.L133:
	fldl	-20(%ebp)
	fsubl	.LC2
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%eax
	fldcw	-22(%ebp)
	leal	-2147483648(%eax),%eax
	jmp	.L135
.L134:
	fldl	-20(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%eax
	fldcw	-22(%ebp)
.L135:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s2
	call	_printf
	leal	16(%esp),%esp
	pushl	.LC14+4
	pushl	.LC14
	pushl	.LC14+4
	pushl	.LC14
	pushl	.LC13
	call	_ful
	leal	20(%esp),%esp
	movsbl	_scld1,%eax
	pushl	%eax
	movsbl	_scd1,%eax
	pushl	%eax
	movsbl	_scf1,%eax
	pushl	%eax
	pushl	$_s1
	call	_printf
	leal	16(%esp),%esp
	flds	.LC15
	fchs
	fstps	-4(%ebp)
	fldl	.LC16
	fchs
	fstpl	-12(%ebp)
	fldl	.LC16
	fchs
	fstpl	-20(%ebp)
	fldl	-20(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movb	-28(%ebp),%al
	fldcw	-22(%ebp)
	movsbl	%al,%eax
	pushl	%eax
	fldl	-12(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movb	-28(%ebp),%al
	fldcw	-22(%ebp)
	movsbl	%al,%eax
	pushl	%eax
	flds	-4(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movb	-28(%ebp),%al
	fldcw	-22(%ebp)
	movsbl	%al,%eax
	pushl	%eax
	pushl	$_s1
	call	_printf
	leal	16(%esp),%esp
	fldl	-20(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movb	-28(%ebp),%al
	fldcw	-22(%ebp)
	movsbl	%al,%eax
	pushl	%eax
	fldl	-12(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movb	-28(%ebp),%al
	fldcw	-22(%ebp)
	movsbl	%al,%eax
	pushl	%eax
	flds	-4(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movb	-28(%ebp),%al
	fldcw	-22(%ebp)
	movsbl	%al,%eax
	pushl	%eax
	pushl	$_s1
	call	_printf
	leal	16(%esp),%esp
	fldl	.LC16
	fchs
	sub	$8,%esp
	fstpl	(%esp)
	fldl	.LC16
	fchs
	sub	$8,%esp
	fstpl	(%esp)
	flds	.LC15
	fchs
	sub	$4,%esp
	fstps	(%esp)
	call	_fsc
	leal	20(%esp),%esp
	movzbl	_ucld1,%eax
	pushl	%eax
	movzbl	_ucd1,%eax
	pushl	%eax
	movzbl	_ucf1,%eax
	pushl	%eax
	pushl	$_s1
	call	_printf
	leal	16(%esp),%esp
	flds	.LC17
	fchs
	fstps	-4(%ebp)
	fldl	.LC18
	fchs
	fstpl	-12(%ebp)
	fldl	.LC18
	fchs
	fstpl	-20(%ebp)
	flds	-4(%ebp)
	fcomps	.LC1
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L137
.L136:
	flds	-4(%ebp)
	fsubs	.LC1
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movb	-28(%ebp),%cl
	fldcw	-22(%ebp)
	jmp	.L138
.L137:
	flds	-4(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movb	-28(%ebp),%cl
	fldcw	-22(%ebp)
.L138:
	fldl	-12(%ebp)
	fcompl	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L140
.L139:
	fldl	-12(%ebp)
	fsubl	.LC2
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movb	-28(%ebp),%ch
	fldcw	-22(%ebp)
	jmp	.L141
.L140:
	fldl	-12(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movb	-28(%ebp),%ch
	fldcw	-22(%ebp)
.L141:
	fldl	-20(%ebp)
	fcompl	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L143
.L142:
	fldl	-20(%ebp)
	fsubl	.LC2
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movb	-28(%ebp),%al
	fldcw	-22(%ebp)
	jmp	.L144
.L143:
	fldl	-20(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movb	-28(%ebp),%al
	fldcw	-22(%ebp)
.L144:
	movzbl	%al,%eax
	pushl	%eax
	movzbl	%ch,%eax
	pushl	%eax
	movzbl	%cl,%eax
	pushl	%eax
	pushl	$_s1
	call	_printf
	leal	16(%esp),%esp
	flds	-4(%ebp)
	fcomps	.LC1
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L146
.L145:
	flds	-4(%ebp)
	fsubs	.LC1
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movb	-28(%ebp),%cl
	fldcw	-22(%ebp)
	jmp	.L147
.L146:
	flds	-4(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movb	-28(%ebp),%cl
	fldcw	-22(%ebp)
.L147:
	fldl	-12(%ebp)
	fcompl	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L149
.L148:
	fldl	-12(%ebp)
	fsubl	.LC2
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movb	-28(%ebp),%ch
	fldcw	-22(%ebp)
	jmp	.L150
.L149:
	fldl	-12(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movb	-28(%ebp),%ch
	fldcw	-22(%ebp)
.L150:
	fldl	-20(%ebp)
	fcompl	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L152
.L151:
	fldl	-20(%ebp)
	fsubl	.LC2
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movb	-28(%ebp),%al
	fldcw	-22(%ebp)
	jmp	.L153
.L152:
	fldl	-20(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movb	-28(%ebp),%al
	fldcw	-22(%ebp)
.L153:
	movzbl	%al,%eax
	pushl	%eax
	movzbl	%ch,%eax
	pushl	%eax
	movzbl	%cl,%eax
	pushl	%eax
	pushl	$_s1
	call	_printf
	leal	16(%esp),%esp
	fldl	.LC18
	fchs
	sub	$8,%esp
	fstpl	(%esp)
	fldl	.LC18
	fchs
	sub	$8,%esp
	fstpl	(%esp)
	flds	.LC17
	fchs
	sub	$4,%esp
	fstps	(%esp)
	call	_fuc
	leal	20(%esp),%esp
	movswl	_sld1,%eax
	pushl	%eax
	movswl	_sd1,%eax
	pushl	%eax
	movswl	_sf1,%eax
	pushl	%eax
	pushl	$_s1
	call	_printf
	leal	16(%esp),%esp
	flds	.LC19
	fchs
	fstps	-4(%ebp)
	fldl	.LC20
	fchs
	fstpl	-12(%ebp)
	fldl	.LC20
	fchs
	fstpl	-20(%ebp)
	fldl	-20(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movw	-28(%ebp),%ax
	fldcw	-22(%ebp)
	movswl	%ax,%eax
	pushl	%eax
	fldl	-12(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movw	-28(%ebp),%ax
	fldcw	-22(%ebp)
	movswl	%ax,%eax
	pushl	%eax
	flds	-4(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movw	-28(%ebp),%ax
	fldcw	-22(%ebp)
	movswl	%ax,%eax
	pushl	%eax
	pushl	$_s1
	call	_printf
	leal	16(%esp),%esp
	fldl	-20(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movw	-28(%ebp),%ax
	fldcw	-22(%ebp)
	movswl	%ax,%eax
	pushl	%eax
	fldl	-12(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movw	-28(%ebp),%ax
	fldcw	-22(%ebp)
	movswl	%ax,%eax
	pushl	%eax
	flds	-4(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movw	-28(%ebp),%ax
	fldcw	-22(%ebp)
	movswl	%ax,%eax
	pushl	%eax
	pushl	$_s1
	call	_printf
	leal	16(%esp),%esp
	fldl	.LC20
	fchs
	sub	$8,%esp
	fstpl	(%esp)
	fldl	.LC20
	fchs
	sub	$8,%esp
	fstpl	(%esp)
	flds	.LC19
	fchs
	sub	$4,%esp
	fstps	(%esp)
	call	_fs
	leal	20(%esp),%esp
	movzwl	_usld1,%eax
	pushl	%eax
	movzwl	_usd1,%eax
	pushl	%eax
	movzwl	_usf1,%eax
	pushl	%eax
	pushl	$_s1
	call	_printf
	leal	16(%esp),%esp
	flds	.LC17
	fchs
	fstps	-4(%ebp)
	fldl	.LC18
	fchs
	fstpl	-12(%ebp)
	fldl	.LC18
	fchs
	fstpl	-20(%ebp)
	flds	-4(%ebp)
	fcomps	.LC1
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L155
.L154:
	flds	-4(%ebp)
	fsubs	.LC1
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%dx
	orw	$3072,%dx
	movw	%dx,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movw	-28(%ebp),%dx
	fldcw	-22(%ebp)
	jmp	.L156
.L155:
	flds	-4(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%dx
	orw	$3072,%dx
	movw	%dx,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movw	-28(%ebp),%dx
	fldcw	-22(%ebp)
.L156:
	fldl	-12(%ebp)
	fcompl	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L158
.L157:
	fldl	-12(%ebp)
	fsubl	.LC2
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%cx
	orw	$3072,%cx
	movw	%cx,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movw	-28(%ebp),%cx
	fldcw	-22(%ebp)
	jmp	.L159
.L158:
	fldl	-12(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%cx
	orw	$3072,%cx
	movw	%cx,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movw	-28(%ebp),%cx
	fldcw	-22(%ebp)
.L159:
	fldl	-20(%ebp)
	fcompl	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L161
.L160:
	fldl	-20(%ebp)
	fsubl	.LC2
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movw	-28(%ebp),%ax
	fldcw	-22(%ebp)
	jmp	.L162
.L161:
	fldl	-20(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movw	-28(%ebp),%ax
	fldcw	-22(%ebp)
.L162:
	movzwl	%ax,%eax
	pushl	%eax
	movzwl	%cx,%eax
	pushl	%eax
	movzwl	%dx,%eax
	pushl	%eax
	pushl	$_s1
	call	_printf
	leal	16(%esp),%esp
	flds	-4(%ebp)
	fcomps	.LC1
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L164
.L163:
	flds	-4(%ebp)
	fsubs	.LC1
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%dx
	orw	$3072,%dx
	movw	%dx,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movw	-28(%ebp),%dx
	fldcw	-22(%ebp)
	jmp	.L165
.L164:
	flds	-4(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%dx
	orw	$3072,%dx
	movw	%dx,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movw	-28(%ebp),%dx
	fldcw	-22(%ebp)
.L165:
	fldl	-12(%ebp)
	fcompl	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L167
.L166:
	fldl	-12(%ebp)
	fsubl	.LC2
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%cx
	orw	$3072,%cx
	movw	%cx,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movw	-28(%ebp),%cx
	fldcw	-22(%ebp)
	jmp	.L168
.L167:
	fldl	-12(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%cx
	orw	$3072,%cx
	movw	%cx,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movw	-28(%ebp),%cx
	fldcw	-22(%ebp)
.L168:
	fldl	-20(%ebp)
	fcompl	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L170
.L169:
	fldl	-20(%ebp)
	fsubl	.LC2
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movw	-28(%ebp),%ax
	fldcw	-22(%ebp)
	jmp	.L171
.L170:
	fldl	-20(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movw	-28(%ebp),%ax
	fldcw	-22(%ebp)
.L171:
	movzwl	%ax,%eax
	pushl	%eax
	movzwl	%cx,%eax
	pushl	%eax
	movzwl	%dx,%eax
	pushl	%eax
	pushl	$_s1
	call	_printf
	leal	16(%esp),%esp
	fldl	.LC18
	fchs
	sub	$8,%esp
	fstpl	(%esp)
	fldl	.LC18
	fchs
	sub	$8,%esp
	fstpl	(%esp)
	flds	.LC17
	fchs
	sub	$4,%esp
	fstps	(%esp)
	call	_fus
	leal	20(%esp),%esp
	pushl	_ild1
	pushl	_id1
	pushl	_if1
	pushl	$_s1
	call	_printf
	leal	16(%esp),%esp
	flds	.LC11
	fchs
	fstps	-4(%ebp)
	fldl	.LC21
	fchs
	fstpl	-12(%ebp)
	fldl	.LC21
	fchs
	fstpl	-20(%ebp)
	fldl	-20(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%eax
	fldcw	-22(%ebp)
	pushl	%eax
	fldl	-12(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%eax
	fldcw	-22(%ebp)
	pushl	%eax
	flds	-4(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%eax
	fldcw	-22(%ebp)
	pushl	%eax
	pushl	$_s1
	call	_printf
	leal	16(%esp),%esp
	fldl	-20(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%eax
	fldcw	-22(%ebp)
	pushl	%eax
	fldl	-12(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%eax
	fldcw	-22(%ebp)
	pushl	%eax
	flds	-4(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%eax
	fldcw	-22(%ebp)
	pushl	%eax
	pushl	$_s1
	call	_printf
	leal	16(%esp),%esp
	fldl	.LC21
	fchs
	sub	$8,%esp
	fstpl	(%esp)
	fldl	.LC21
	fchs
	sub	$8,%esp
	fstpl	(%esp)
	flds	.LC11
	fchs
	sub	$4,%esp
	fstps	(%esp)
	call	_fi
	leal	20(%esp),%esp
	pushl	_uild1
	pushl	_uid1
	pushl	_uif1
	pushl	$_s1
	call	_printf
	leal	16(%esp),%esp
	flds	.LC17
	fchs
	fstps	-4(%ebp)
	fldl	.LC18
	fchs
	fstpl	-12(%ebp)
	fldl	.LC18
	fchs
	fstpl	-20(%ebp)
	flds	-4(%ebp)
	fcomps	.LC1
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L173
.L172:
	flds	-4(%ebp)
	fsubs	.LC1
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%eax
	fldcw	-22(%ebp)
	leal	-2147483648(%eax),%edx
	jmp	.L174
.L173:
	flds	-4(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%dx
	orw	$3072,%dx
	movw	%dx,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%edx
	fldcw	-22(%ebp)
.L174:
	fldl	-12(%ebp)
	fcompl	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L176
.L175:
	fldl	-12(%ebp)
	fsubl	.LC2
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%eax
	fldcw	-22(%ebp)
	leal	-2147483648(%eax),%ecx
	jmp	.L177
.L176:
	fldl	-12(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%cx
	orw	$3072,%cx
	movw	%cx,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%ecx
	fldcw	-22(%ebp)
.L177:
	fldl	-20(%ebp)
	fcompl	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L179
.L178:
	fldl	-20(%ebp)
	fsubl	.LC2
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%eax
	fldcw	-22(%ebp)
	leal	-2147483648(%eax),%eax
	jmp	.L180
.L179:
	fldl	-20(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%eax
	fldcw	-22(%ebp)
.L180:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s1
	call	_printf
	leal	16(%esp),%esp
	flds	-4(%ebp)
	fcomps	.LC1
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L182
.L181:
	flds	-4(%ebp)
	fsubs	.LC1
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%eax
	fldcw	-22(%ebp)
	leal	-2147483648(%eax),%edx
	jmp	.L183
.L182:
	flds	-4(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%dx
	orw	$3072,%dx
	movw	%dx,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%edx
	fldcw	-22(%ebp)
.L183:
	fldl	-12(%ebp)
	fcompl	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L185
.L184:
	fldl	-12(%ebp)
	fsubl	.LC2
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%eax
	fldcw	-22(%ebp)
	leal	-2147483648(%eax),%ecx
	jmp	.L186
.L185:
	fldl	-12(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%cx
	orw	$3072,%cx
	movw	%cx,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%ecx
	fldcw	-22(%ebp)
.L186:
	fldl	-20(%ebp)
	fcompl	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L188
.L187:
	fldl	-20(%ebp)
	fsubl	.LC2
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%eax
	fldcw	-22(%ebp)
	leal	-2147483648(%eax),%eax
	jmp	.L189
.L188:
	fldl	-20(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%eax
	fldcw	-22(%ebp)
.L189:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s1
	call	_printf
	leal	16(%esp),%esp
	fldl	.LC18
	fchs
	sub	$8,%esp
	fstpl	(%esp)
	fldl	.LC18
	fchs
	sub	$8,%esp
	fstpl	(%esp)
	flds	.LC17
	fchs
	sub	$4,%esp
	fstps	(%esp)
	call	_fui
	leal	20(%esp),%esp
	pushl	_lld1
	pushl	_ld1
	pushl	_lf1
	pushl	$_s1
	call	_printf
	leal	16(%esp),%esp
	flds	.LC11
	fchs
	fstps	-4(%ebp)
	fldl	.LC21
	fchs
	fstpl	-12(%ebp)
	fldl	.LC21
	fchs
	fstpl	-20(%ebp)
	fldl	-20(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%eax
	fldcw	-22(%ebp)
	pushl	%eax
	fldl	-12(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%eax
	fldcw	-22(%ebp)
	pushl	%eax
	flds	-4(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%eax
	fldcw	-22(%ebp)
	pushl	%eax
	pushl	$_s2
	call	_printf
	leal	16(%esp),%esp
	fldl	-20(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%eax
	fldcw	-22(%ebp)
	pushl	%eax
	fldl	-12(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%eax
	fldcw	-22(%ebp)
	pushl	%eax
	flds	-4(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%eax
	fldcw	-22(%ebp)
	pushl	%eax
	pushl	$_s2
	call	_printf
	leal	16(%esp),%esp
	fldl	.LC21
	fchs
	sub	$8,%esp
	fstpl	(%esp)
	fldl	.LC21
	fchs
	sub	$8,%esp
	fstpl	(%esp)
	flds	.LC11
	fchs
	sub	$4,%esp
	fstps	(%esp)
	call	_fl
	leal	20(%esp),%esp
	pushl	_ulld1
	pushl	_uld1
	pushl	_ulf1
	pushl	$_s1
	call	_printf
	leal	16(%esp),%esp
	flds	.LC17
	fchs
	fstps	-4(%ebp)
	fldl	.LC18
	fchs
	fstpl	-12(%ebp)
	fldl	.LC18
	fchs
	fstpl	-20(%ebp)
	flds	-4(%ebp)
	fcomps	.LC1
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L191
.L190:
	flds	-4(%ebp)
	fsubs	.LC1
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%eax
	fldcw	-22(%ebp)
	leal	-2147483648(%eax),%edx
	jmp	.L192
.L191:
	flds	-4(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%dx
	orw	$3072,%dx
	movw	%dx,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%edx
	fldcw	-22(%ebp)
.L192:
	fldl	-12(%ebp)
	fcompl	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L194
.L193:
	fldl	-12(%ebp)
	fsubl	.LC2
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%eax
	fldcw	-22(%ebp)
	leal	-2147483648(%eax),%ecx
	jmp	.L195
.L194:
	fldl	-12(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%cx
	orw	$3072,%cx
	movw	%cx,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%ecx
	fldcw	-22(%ebp)
.L195:
	fldl	-20(%ebp)
	fcompl	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L197
.L196:
	fldl	-20(%ebp)
	fsubl	.LC2
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%eax
	fldcw	-22(%ebp)
	leal	-2147483648(%eax),%eax
	jmp	.L198
.L197:
	fldl	-20(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%eax
	fldcw	-22(%ebp)
.L198:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s2
	call	_printf
	leal	16(%esp),%esp
	flds	-4(%ebp)
	fcomps	.LC1
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L200
.L199:
	flds	-4(%ebp)
	fsubs	.LC1
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%eax
	fldcw	-22(%ebp)
	leal	-2147483648(%eax),%edx
	jmp	.L201
.L200:
	flds	-4(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%dx
	orw	$3072,%dx
	movw	%dx,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%edx
	fldcw	-22(%ebp)
.L201:
	fldl	-12(%ebp)
	fcompl	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L203
.L202:
	fldl	-12(%ebp)
	fsubl	.LC2
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%eax
	fldcw	-22(%ebp)
	leal	-2147483648(%eax),%ecx
	jmp	.L204
.L203:
	fldl	-12(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%cx
	orw	$3072,%cx
	movw	%cx,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%ecx
	fldcw	-22(%ebp)
.L204:
	fldl	-20(%ebp)
	fcompl	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L206
.L205:
	fldl	-20(%ebp)
	fsubl	.LC2
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%eax
	fldcw	-22(%ebp)
	leal	-2147483648(%eax),%eax
	jmp	.L207
.L206:
	fldl	-20(%ebp)
	fnstcw	-22(%ebp)
	movw	-22(%ebp),%ax
	orw	$3072,%ax
	movw	%ax,-24(%ebp)
	fldcw	-24(%ebp)
	fistpl	-28(%ebp)
	movl	-28(%ebp),%eax
	fldcw	-22(%ebp)
.L207:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s2
	call	_printf
	leal	16(%esp),%esp
	fldl	.LC18
	fchs
	sub	$8,%esp
	fstpl	(%esp)
	fldl	.LC18
	fchs
	sub	$8,%esp
	fstpl	(%esp)
	flds	.LC17
	fchs
	sub	$4,%esp
	fstps	(%esp)
	call	_ful
	leal	20(%esp),%esp
	movl	$0,%eax
	leave
	ret

	.data
	.align	1
	.global	_s1
_s1:
	.byte	37
	.byte	48
	.byte	56
	.byte	88
	.byte	32
	.byte	37
	.byte	48
	.byte	56
	.byte	88
	.byte	32
	.byte	37
	.byte	48
	.byte	56
	.byte	88
	.byte	10
	.byte	0
	.align	1
	.global	_s2
_s2:
	.byte	37
	.byte	48
	.byte	56
	.byte	108
	.byte	88
	.byte	32
	.byte	37
	.byte	48
	.byte	56
	.byte	108
	.byte	88
	.byte	32
	.byte	37
	.byte	48
	.byte	56
	.byte	108
	.byte	88
	.byte	10
	.byte	0
	.align	1
	.global	_scf0
_scf0:
	.byte	127
	.align	1
	.global	_scd0
_scd0:
	.byte	127
	.align	1
	.global	_scld0
_scld0:
	.byte	127
	.align	1
	.global	_ucf0
_ucf0:
	.byte	-1
	.align	1
	.global	_ucd0
_ucd0:
	.byte	-1
	.align	1
	.global	_ucld0
_ucld0:
	.byte	-1
	.align	2
	.global	_sf0
_sf0:
	.short	32767
	.align	2
	.global	_sd0
_sd0:
	.short	32767
	.align	2
	.global	_sld0
_sld0:
	.short	32767
	.align	2
	.global	_usf0
_usf0:
	.short	-1
	.align	2
	.global	_usd0
_usd0:
	.short	-1
	.align	2
	.global	_usld0
_usld0:
	.short	-1
	.align	4
	.global	_if0
_if0:
	.long	2147483520
	.align	4
	.global	_id0
_id0:
	.long	2147483647
	.align	4
	.global	_ild0
_ild0:
	.long	2147483647
	.align	4
	.global	_uif0
_uif0:
	.long	-256
	.align	4
	.global	_uid0
_uid0:
	.long	-1
	.align	4
	.global	_uild0
_uild0:
	.long	-1
	.align	4
	.global	_lf0
_lf0:
	.long	2147483520
	.align	4
	.global	_ld0
_ld0:
	.long	2147483647
	.align	4
	.global	_lld0
_lld0:
	.long	2147483647
	.align	4
	.global	_ulf0
_ulf0:
	.long	-256
	.align	4
	.global	_uld0
_uld0:
	.long	-1
	.align	4
	.global	_ulld0
_ulld0:
	.long	-1
	.align	1
	.global	_scf1
_scf1:
	.byte	-128
	.align	1
	.global	_scd1
_scd1:
	.byte	-128
	.align	1
	.global	_scld1
_scld1:
	.byte	-128
	.align	1
	.global	_ucf1
_ucf1:
	.byte	0
	.align	1
	.global	_ucd1
_ucd1:
	.byte	0
	.align	1
	.global	_ucld1
_ucld1:
	.byte	0
	.align	2
	.global	_sf1
_sf1:
	.short	-32768
	.align	2
	.global	_sd1
_sd1:
	.short	-32768
	.align	2
	.global	_sld1
_sld1:
	.short	-32768
	.align	2
	.global	_usf1
_usf1:
	.short	0
	.align	2
	.global	_usd1
_usd1:
	.short	0
	.align	2
	.global	_usld1
_usld1:
	.short	0
	.align	4
	.global	_if1
_if1:
	.long	-2147483520
	.align	4
	.global	_id1
_id1:
	.long	-2147483648
	.align	4
	.global	_ild1
_ild1:
	.long	-2147483648
	.align	4
	.global	_uif1
_uif1:
	.long	0
	.align	4
	.global	_uid1
_uid1:
	.long	0
	.align	4
	.global	_uild1
_uild1:
	.long	0
	.align	4
	.global	_lf1
_lf1:
	.long	-2147483520
	.align	4
	.global	_ld1
_ld1:
	.long	-2147483648
	.align	4
	.global	_lld1
_lld1:
	.long	-2147483648
	.align	4
	.global	_ulf1
_ulf1:
	.long	0
	.align	4
	.global	_uld1
_uld1:
	.long	0
	.align	4
	.global	_ulld1
_ulld1:
	.long	0
	.text
	.align	4
.LC1:
	.long	0x4f000000 /* 2.147483648E9 */
	.align	4
.LC2:
	.long	0x0,0x41e00000 /* 2.147483648E9 */
	.align	4
.LC3:
	.long	0x42ffcccd /* 127.9000015258789 */
	.align	4
.LC4:
	.long	0x9999999a,0x405ff999 /* 127.9 */
	.align	4
.LC5:
	.long	0x437fe666 /* 255.89999389648438 */
	.align	4
.LC6:
	.long	0xcccccccd,0x406ffccc /* 255.9 */
	.align	4
.LC7:
	.long	0x46ffffcd /* 32767.900390625 */
	.align	4
.LC8:
	.long	0x9999999a,0x40dffff9 /* 32767.9 */
	.align	4
.LC9:
	.long	0x477fffe6 /* 65535.8984375 */
	.align	4
.LC10:
	.long	0xcccccccd,0x40effffc /* 65535.9 */
	.align	4
.LC11:
	.long	0x4effffff /* 2.14748352E9 */
	.align	4
.LC12:
	.long	0xfff9999a,0x41dfffff /* 2.1474836479E9 */
	.align	4
.LC13:
	.long	0x4f7fffff /* 4.29496704E9 */
	.align	4
.LC14:
	.long	0xfffccccd,0x41efffff /* 4.2949672959E9 */
	.align	4
.LC15:
	.long	0x4300e666 /* 128.89999389648438 */
	.align	4
.LC16:
	.long	0xcccccccd,0x40601ccc /* 128.9 */
	.align	4
.LC17:
	.long	0x3f666666 /* 0.8999999761581421 */
	.align	4
.LC18:
	.long	0xcccccccd,0x3feccccc /* 0.9 */
	.align	4
.LC19:
	.long	0x470000e6 /* 32768.8984375 */
	.align	4
.LC20:
	.long	0xcccccccd,0x40e0001c /* 32768.9 */
	.align	4
.LC21:
	.long	0x1ccccd,0x41e00000 /* 2.1474836489E9 */
