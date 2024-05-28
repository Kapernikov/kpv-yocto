## taken from wic
python do_evaluate_template_files() {
    import re
    __expand_var_regexp__ = re.compile(r"\${[^{}@\n\t :]+}")

    def expand_line(line):
        while True:
            m = __expand_var_regexp__.search(line)
            if not m:
                return line
            key = m.group()[2:-1]
            val = d.getVar(key,"\$\{" + key + "\}")
            if val is None:
                logger.warning("cannot expand variable %s" % key)
                return line
            line = line[:m.start()] + val + line[m.end():]

    def expand_file(src, dst):
        with open(dst, 'w') as outp:
            with open(src) as inp:
                lineno = 0
                for line in inp:
                    lineno += 1
                    if line and line[0] != '#':
                        line = expand_line(line)
                    outp.write(line)

    import os
    srcdir = d.getVar('WORKDIR')
    for filename in os.listdir(srcdir):
        if filename.endswith('.in'):
            infile = os.path.join(srcdir, filename)
            outfile = os.path.join(srcdir, filename[:-3])  # Remove .in extension
            expand_file(infile,outfile)

}

addtask evaluate_template_files before do_install
