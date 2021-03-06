# Cell width for easy viewing
from IPython.core.display import display, HTML
display(HTML("<style>.container { width:90% !important; }</style>"))

# Libraries
import pandas as pd
import numpy as np

print ("""To plot, execute the following:
from matplotlib import pyplot as mplt
from pandas.tools.plotting import parallel_coordinates
from scipy import stats

import seaborn as sns
sns.set_palette('muted')
sns.set_style('ticks')
sns.set_context('notebook')""")

# Global Variables
metrics = ['cost', 'expanded', 'branching', 'time']
column_names = ['size', 'boardID', 'algo', 'heur',  'expanded', 'branching', 'cost', 'time']
dtypes = {
    'cost': int,
    'expanded': int
}

# Directories
results_dir = '../../data/results/'
canonical_dir = results_dir + 'canonical/'
plots_dir = '../../docs/analysis/'

# files
djPDB_h_f = results_dir + 'disjointPDBHorizontal.csv'
djPDB_v_f = results_dir + 'disjointPDBVertical.csv'
djPDB_m_f = results_dir + 'disjointPDBMax.csv'

linC_f = results_dir + 'linearConflict.csv'
ida_f = results_dir + 'ida-linearConflict.csv'
manhat_f = results_dir + 'manhattan.csv'
nmax_f = results_dir + 'NMaxSwap.csv'

nAdd_m_f = results_dir + 'nonAdditiveMax.csv'
nAdd_f_f = results_dir + 'nonAdditiveFringe.csv'

# Final results
combined_f = canonical_dir + 'combined.csv'
parcord_f = canonical_dir + 'parcord.csv'
algos_f = canonical_dir + 'algos.csv'

# All together
files = [
    djPDB_h_f, djPDB_v_f, djPDB_m_f,
    linC_f, ida_f,
    manhat_f, nmax_f,
    nAdd_m_f, nAdd_f_f
    ]
