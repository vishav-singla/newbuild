from dataclasses import dataclass
from datetime import date

@dataclass(frozen=True)
class SaleRecord:
    """Immutable data model representing a single sales transaction."""
    date: date
    product: str
    category: str
    amount: float
    region: str

