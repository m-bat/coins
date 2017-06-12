 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */
	.text
	.align	1
_string.22:
	.byte	84
	.byte	0
	.align	1
_string.24:
	.byte	45
	.byte	0
	.align	1
_string.28:
	.byte	10
	.byte	0

	.align	4
	.global	_f0
_f0:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	movl	8(%ebp),%ebx
	cmpl	$_s,%ebx
	jl	.L4
.L3:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L5
.L4:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L5:
	cmpl	$_s+4,%ebx
	jl	.L7
.L6:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L8
.L7:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L8:
	pushl	$_string.28
	call	_printf
	leal	4(%esp),%esp
.L9:
	popl	%ebx
	leave
	ret


	.align	4
	.global	_g0
_g0:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	movl	8(%ebp),%ebx
	cmpl	$_u,%ebx
	jl	.L13
.L12:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L14
.L13:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L14:
	cmpl	$_u,%ebx
	jl	.L16
.L15:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L17
.L16:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L17:
	pushl	$_string.28
	call	_printf
	leal	4(%esp),%esp
.L18:
	popl	%ebx
	leave
	ret


	.align	4
	.global	_h0
_h0:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	movl	8(%ebp),%ebx
	cmpl	$_a+8,%ebx
	jl	.L22
.L21:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L23
.L22:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L23:
	cmpl	$_a+16,%ebx
	jl	.L25
.L24:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L26
.L25:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L26:
	pushl	$_string.28
	call	_printf
	leal	4(%esp),%esp
.L27:
	popl	%ebx
	leave
	ret


	.align	4
	.global	_f1
_f1:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	movl	8(%ebp),%ebx
	cmpl	$_s,%ebx
	jg	.L31
.L30:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L32
.L31:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L32:
	cmpl	$_s+4,%ebx
	jg	.L34
.L33:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L35
.L34:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L35:
	pushl	$_string.28
	call	_printf
	leal	4(%esp),%esp
.L36:
	popl	%ebx
	leave
	ret


	.align	4
	.global	_g1
_g1:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	movl	8(%ebp),%ebx
	cmpl	$_u,%ebx
	jg	.L40
.L39:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L41
.L40:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L41:
	cmpl	$_u,%ebx
	jg	.L43
.L42:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L44
.L43:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L44:
	pushl	$_string.28
	call	_printf
	leal	4(%esp),%esp
.L45:
	popl	%ebx
	leave
	ret


	.align	4
	.global	_h1
_h1:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	movl	8(%ebp),%ebx
	cmpl	$_a+8,%ebx
	jg	.L49
.L48:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L50
.L49:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L50:
	cmpl	$_a+16,%ebx
	jg	.L52
.L51:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L53
.L52:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L53:
	pushl	$_string.28
	call	_printf
	leal	4(%esp),%esp
.L54:
	popl	%ebx
	leave
	ret


	.align	4
	.global	_op
_op:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%ecx
	movl	12(%ebp),%eax
	cmpl	%eax,%ecx
	jl	.L58
.L57:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L59
.L58:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L59:
	leave
	ret


	.align	4
	.global	_f2
_f2:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	movl	8(%ebp),%ebx
	pushl	$_s
	pushl	%ebx
	call	_op
	leal	8(%esp),%esp
	pushl	$_s+4
	pushl	%ebx
	call	_op
	leal	8(%esp),%esp
	pushl	$_string.28
	call	_printf
	leal	4(%esp),%esp
.L62:
	popl	%ebx
	leave
	ret


	.align	4
	.global	_g2
_g2:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	movl	8(%ebp),%ebx
	pushl	$_u
	pushl	%ebx
	call	_op
	leal	8(%esp),%esp
	pushl	$_u
	pushl	%ebx
	call	_op
	leal	8(%esp),%esp
	pushl	$_string.28
	call	_printf
	leal	4(%esp),%esp
.L65:
	popl	%ebx
	leave
	ret


	.align	4
	.global	_h2
_h2:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	movl	8(%ebp),%ebx
	pushl	$_a+8
	pushl	%ebx
	call	_op
	leal	8(%esp),%esp
	pushl	$_a+16
	pushl	%ebx
	call	_op
	leal	8(%esp),%esp
	pushl	$_string.28
	call	_printf
	leal	4(%esp),%esp
.L68:
	popl	%ebx
	leave
	ret


	.align	4
	.global	_main0g
_main0g:
	pushl	%ebp
	movl	%esp,%ebp
	movl	$_s,%eax
	cmpl	$_s,%eax
	jl	.L72
.L71:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L73
.L72:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L73:
	movl	$_s,%eax
	cmpl	$_s+4,%eax
	jl	.L75
.L74:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L76
.L75:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L76:
	pushl	$_string.28
	call	_printf
	leal	4(%esp),%esp
	movl	$_s+4,%eax
	cmpl	$_s,%eax
	jl	.L78
.L77:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L79
.L78:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L79:
	movl	$_s+4,%eax
	cmpl	$_s+4,%eax
	jl	.L81
.L80:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L82
.L81:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L82:
	pushl	$_string.28
	call	_printf
	leal	4(%esp),%esp
	movl	$_u,%eax
	cmpl	$_u,%eax
	jl	.L84
.L83:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L85
.L84:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L85:
	movl	$_u,%eax
	cmpl	$_u,%eax
	jl	.L87
.L86:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L88
.L87:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L88:
	pushl	$_string.28
	call	_printf
	leal	4(%esp),%esp
	movl	$_u,%eax
	cmpl	$_u,%eax
	jl	.L90
.L89:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L91
.L90:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L91:
	movl	$_u,%eax
	cmpl	$_u,%eax
	jl	.L93
.L92:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L94
.L93:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L94:
	pushl	$_string.28
	call	_printf
	leal	4(%esp),%esp
	movl	$_a+8,%eax
	cmpl	$_a+8,%eax
	jl	.L96
.L95:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L97
.L96:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L97:
	movl	$_a+8,%eax
	cmpl	$_a+16,%eax
	jl	.L99
.L98:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L100
.L99:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L100:
	pushl	$_string.28
	call	_printf
	leal	4(%esp),%esp
	movl	$_a+16,%eax
	cmpl	$_a+8,%eax
	jl	.L102
.L101:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L103
.L102:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L103:
	movl	$_a+16,%eax
	cmpl	$_a+16,%eax
	jl	.L105
.L104:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L106
.L105:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L106:
	pushl	$_string.28
	call	_printf
	leal	4(%esp),%esp
.L107:
	leave
	ret


	.align	4
	.global	_main0l
_main0l:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$28,%esp
	movl	$0,-28(%ebp)
	movl	$10,-24(%ebp)
	movl	$20,-20(%ebp)
	movl	$30,-16(%ebp)
	leal	-8(%ebp),%ecx
	leal	-8(%ebp),%eax
	cmpl	%eax,%ecx
	jl	.L111
.L110:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L112
.L111:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L112:
	leal	-8(%ebp),%ecx
	leal	-4(%ebp),%eax
	cmpl	%eax,%ecx
	jl	.L114
.L113:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L115
.L114:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L115:
	pushl	$_string.28
	call	_printf
	leal	4(%esp),%esp
	leal	-4(%ebp),%ecx
	leal	-8(%ebp),%eax
	cmpl	%eax,%ecx
	jl	.L117
.L116:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L118
.L117:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L118:
	leal	-4(%ebp),%ecx
	leal	-4(%ebp),%eax
	cmpl	%eax,%ecx
	jl	.L120
.L119:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L121
.L120:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L121:
	pushl	$_string.28
	call	_printf
	leal	4(%esp),%esp
	leal	-12(%ebp),%ecx
	leal	-12(%ebp),%eax
	cmpl	%eax,%ecx
	jl	.L123
.L122:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L124
.L123:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L124:
	leal	-12(%ebp),%ecx
	leal	-12(%ebp),%eax
	cmpl	%eax,%ecx
	jl	.L126
.L125:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L127
.L126:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L127:
	pushl	$_string.28
	call	_printf
	leal	4(%esp),%esp
	leal	-12(%ebp),%ecx
	leal	-12(%ebp),%eax
	cmpl	%eax,%ecx
	jl	.L129
.L128:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L130
.L129:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L130:
	leal	-12(%ebp),%ecx
	leal	-12(%ebp),%eax
	cmpl	%eax,%ecx
	jl	.L132
.L131:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L133
.L132:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L133:
	pushl	$_string.28
	call	_printf
	leal	4(%esp),%esp
	leal	-20(%ebp),%ecx
	leal	-20(%ebp),%eax
	cmpl	%eax,%ecx
	jl	.L135
.L134:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L136
.L135:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L136:
	leal	-20(%ebp),%ecx
	leal	-12(%ebp),%eax
	cmpl	%eax,%ecx
	jl	.L138
.L137:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L139
.L138:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L139:
	pushl	$_string.28
	call	_printf
	leal	4(%esp),%esp
	leal	-12(%ebp),%ecx
	leal	-20(%ebp),%eax
	cmpl	%eax,%ecx
	jl	.L141
.L140:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L142
.L141:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L142:
	leal	-12(%ebp),%ecx
	leal	-12(%ebp),%eax
	cmpl	%eax,%ecx
	jl	.L144
.L143:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L145
.L144:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L145:
	pushl	$_string.28
	call	_printf
	leal	4(%esp),%esp
.L146:
	leave
	ret


	.align	4
	.global	_main1
_main1:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	movl	$_s,%ebx
	cmpl	$_s,%ebx
	jl	.L150
.L149:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L151
.L150:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L151:
	cmpl	$_s,%ebx
	jl	.L153
.L152:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L154
.L153:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L154:
	movl	$_s,%ebx
	cmpl	$_s+4,%ebx
	jl	.L156
.L155:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L157
.L156:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L157:
	cmpl	$_s+4,%ebx
	jl	.L159
.L158:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L160
.L159:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L160:
	pushl	$_string.28
	call	_printf
	leal	4(%esp),%esp
	movl	$_s+4,%ebx
	cmpl	$_s,%ebx
	jl	.L162
.L161:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L163
.L162:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L163:
	cmpl	$_s,%ebx
	jl	.L165
.L164:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L166
.L165:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L166:
	movl	$_s+4,%ebx
	cmpl	$_s+4,%ebx
	jl	.L168
.L167:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L169
.L168:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L169:
	cmpl	$_s+4,%ebx
	jl	.L171
.L170:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L172
.L171:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L172:
	pushl	$_string.28
	call	_printf
	leal	4(%esp),%esp
.L173:
	popl	%ebx
	leave
	ret


	.align	4
	.global	_main2
_main2:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	movl	$_u,%ebx
	cmpl	$_u,%ebx
	jl	.L177
.L176:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L178
.L177:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L178:
	cmpl	$_u,%ebx
	jl	.L180
.L179:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L181
.L180:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L181:
	movl	$_u,%ebx
	cmpl	$_u,%ebx
	jl	.L183
.L182:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L184
.L183:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L184:
	cmpl	$_u,%ebx
	jl	.L186
.L185:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L187
.L186:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L187:
	pushl	$_string.28
	call	_printf
	leal	4(%esp),%esp
	movl	$_u,%ebx
	cmpl	$_u,%ebx
	jl	.L189
.L188:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L190
.L189:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L190:
	cmpl	$_u,%ebx
	jl	.L192
.L191:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L193
.L192:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L193:
	movl	$_u,%ebx
	cmpl	$_u,%ebx
	jl	.L195
.L194:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L196
.L195:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L196:
	cmpl	$_u,%ebx
	jl	.L198
.L197:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L199
.L198:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L199:
	pushl	$_string.28
	call	_printf
	leal	4(%esp),%esp
.L200:
	popl	%ebx
	leave
	ret


	.align	4
	.global	_main3
_main3:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	movl	$_a+8,%ebx
	cmpl	$_a+8,%ebx
	jl	.L204
.L203:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L205
.L204:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L205:
	cmpl	$_a+8,%ebx
	jl	.L207
.L206:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L208
.L207:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L208:
	movl	$_a+8,%ebx
	cmpl	$_a+16,%ebx
	jl	.L210
.L209:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L211
.L210:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L211:
	cmpl	$_a+16,%ebx
	jl	.L213
.L212:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L214
.L213:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L214:
	pushl	$_string.28
	call	_printf
	leal	4(%esp),%esp
	movl	$_a+16,%ebx
	cmpl	$_a+8,%ebx
	jl	.L216
.L215:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L217
.L216:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L217:
	cmpl	$_a+8,%ebx
	jl	.L219
.L218:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L220
.L219:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L220:
	movl	$_a+16,%ebx
	cmpl	$_a+16,%ebx
	jl	.L222
.L221:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L223
.L222:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L223:
	cmpl	$_a+16,%ebx
	jl	.L225
.L224:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L226
.L225:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L226:
	pushl	$_string.28
	call	_printf
	leal	4(%esp),%esp
.L227:
	popl	%ebx
	leave
	ret


	.align	4
	.global	_main4
_main4:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	movl	$_s,%ebx
	cmpl	$_s,%ebx
	jg	.L231
.L230:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L232
.L231:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L232:
	cmpl	$_s,%ebx
	jg	.L234
.L233:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L235
.L234:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L235:
	movl	$_s,%ebx
	cmpl	$_s+4,%ebx
	jg	.L237
.L236:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L238
.L237:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L238:
	cmpl	$_s+4,%ebx
	jg	.L240
.L239:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L241
.L240:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L241:
	pushl	$_string.28
	call	_printf
	leal	4(%esp),%esp
	movl	$_s+4,%ebx
	cmpl	$_s,%ebx
	jg	.L243
.L242:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L244
.L243:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L244:
	cmpl	$_s,%ebx
	jg	.L246
.L245:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L247
.L246:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L247:
	movl	$_s+4,%ebx
	cmpl	$_s+4,%ebx
	jg	.L249
.L248:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L250
.L249:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L250:
	cmpl	$_s+4,%ebx
	jg	.L252
.L251:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L253
.L252:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L253:
	pushl	$_string.28
	call	_printf
	leal	4(%esp),%esp
.L254:
	popl	%ebx
	leave
	ret


	.align	4
	.global	_main5
_main5:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	movl	$_u,%ebx
	cmpl	$_u,%ebx
	jg	.L258
.L257:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L259
.L258:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L259:
	cmpl	$_u,%ebx
	jg	.L261
.L260:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L262
.L261:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L262:
	movl	$_u,%ebx
	cmpl	$_u,%ebx
	jg	.L264
.L263:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L265
.L264:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L265:
	cmpl	$_u,%ebx
	jg	.L267
.L266:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L268
.L267:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L268:
	pushl	$_string.28
	call	_printf
	leal	4(%esp),%esp
	movl	$_u,%ebx
	cmpl	$_u,%ebx
	jg	.L270
.L269:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L271
.L270:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L271:
	cmpl	$_u,%ebx
	jg	.L273
.L272:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L274
.L273:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L274:
	movl	$_u,%ebx
	cmpl	$_u,%ebx
	jg	.L276
.L275:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L277
.L276:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L277:
	cmpl	$_u,%ebx
	jg	.L279
.L278:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L280
.L279:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L280:
	pushl	$_string.28
	call	_printf
	leal	4(%esp),%esp
.L281:
	popl	%ebx
	leave
	ret


	.align	4
	.global	_main6
_main6:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	movl	$_a+8,%ebx
	cmpl	$_a+8,%ebx
	jg	.L285
.L284:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L286
.L285:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L286:
	cmpl	$_a+8,%ebx
	jg	.L288
.L287:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L289
.L288:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L289:
	movl	$_a+8,%ebx
	cmpl	$_a+16,%ebx
	jg	.L291
.L290:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L292
.L291:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L292:
	cmpl	$_a+16,%ebx
	jg	.L294
.L293:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L295
.L294:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L295:
	pushl	$_string.28
	call	_printf
	leal	4(%esp),%esp
	movl	$_a+16,%ebx
	cmpl	$_a+8,%ebx
	jg	.L297
.L296:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L298
.L297:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L298:
	cmpl	$_a+8,%ebx
	jg	.L300
.L299:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L301
.L300:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L301:
	movl	$_a+16,%ebx
	cmpl	$_a+16,%ebx
	jg	.L303
.L302:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L304
.L303:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L304:
	cmpl	$_a+16,%ebx
	jg	.L306
.L305:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L307
.L306:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L307:
	pushl	$_string.28
	call	_printf
	leal	4(%esp),%esp
.L308:
	popl	%ebx
	leave
	ret


	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	call	_main0g
	call	_main0l
	call	_main1
	call	_main2
	call	_main3
	call	_main4
	call	_main5
	call	_main6
	pushl	$_s
	call	_f0
	leal	4(%esp),%esp
	pushl	$_s+4
	call	_f0
	leal	4(%esp),%esp
	pushl	$_u
	call	_g0
	leal	4(%esp),%esp
	pushl	$_u
	call	_g0
	leal	4(%esp),%esp
	pushl	$_a+8
	call	_h0
	leal	4(%esp),%esp
	pushl	$_a+16
	call	_h0
	leal	4(%esp),%esp
	pushl	$_s
	call	_f1
	leal	4(%esp),%esp
	pushl	$_s+4
	call	_f1
	leal	4(%esp),%esp
	pushl	$_u
	call	_g1
	leal	4(%esp),%esp
	pushl	$_u
	call	_g1
	leal	4(%esp),%esp
	pushl	$_a+8
	call	_h1
	leal	4(%esp),%esp
	pushl	$_a+16
	call	_h1
	leal	4(%esp),%esp
	pushl	$_s
	call	_f2
	leal	4(%esp),%esp
	pushl	$_s+4
	call	_f2
	leal	4(%esp),%esp
	pushl	$_u
	call	_g2
	leal	4(%esp),%esp
	pushl	$_u
	call	_g2
	leal	4(%esp),%esp
	pushl	$_a+8
	call	_h2
	leal	4(%esp),%esp
	pushl	$_a+16
	call	_h2
	leal	4(%esp),%esp
	movl	$0,%eax
	leave
	ret

	.data
	.align	1
	.global	_s3
_s3:
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
	.global	_a
_a:
	.long	0
	.long	10
	.long	20
	.long	30
	.comm	_s,8
	.comm	_u,4
