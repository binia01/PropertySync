import { PrismaClient } from '@prisma/client';
import * as argon from 'argon2';

const prisma = new PrismaClient();

async function main() {
	const hash = await argon.hash('password123');

  // Sellers with properties
  const sellersData = [
    {
      email: 'seller1@example.com',
      firstname: 'Alice',
      lastname: 'Smith',
      role: 'SELLER',
      properties: [
        {
          title: 'Modern Loft in LA',
          description: 'A spacious loft with lots of natural light.',
          price: 450000,
          location: 'Los Angeles, CA',
          beds: 4,
          baths: 6,
          area: 300
        },
        {
          title: 'Beach House Retreat',
          description: 'Relaxing 3-bedroom beach house with ocean views.',
          price: 750000,
          location: 'Malibu, CA',
          beds: 4,
          baths: 6,
          area: 300
        }
      ],
    },
    {
      email: 'seller2@example.com',
      firstname: 'Carlos',
      lastname: 'Martinez',
      role: 'SELLER',
      properties: [
        {
          title: 'Suburban Family Home',
          description: 'Comfortable 4-bedroom home in a quiet neighborhood.',
          price: 350000,
          location: 'Austin, TX',
          beds: 4,
          baths: 6,
          area: 300
        }
      ],
    },
    {
      email: 'seller3@example.com',
      firstname: 'Fatima',
      lastname: 'Ali',
      role: 'SELLER',
      properties: [
        {
          title: 'Downtown Condo',
          description: 'Compact and convenient city-living experience.',
          price: 300000,
          location: 'Chicago, IL',
          beds: 4,
          baths: 6,
          area: 300
        }
      ],
    },
  ];

  for (const seller of sellersData) {
    await prisma.user.create({
      data: {
        email: seller.email,
        firstname: seller.firstname,
        lastname: seller.lastname,
        hash,
        role: 'SELLER',
        properties: {
          create: seller.properties.map((p) => ({
            ...p,
            status: 'ACTIVE',
          })),
        },
      },
    });
  }

  // Buyers (no properties)
  const buyers = [
    {
      email: 'buyer1@example.com',
      firstname: 'Emma',
      lastname: 'Wong',
    },
    {
      email: 'buyer2@example.com',
      firstname: 'Liam',
      lastname: 'Patel',
    },
  ];

  for (const buyer of buyers) {
    await prisma.user.create({
      data: {
        email: buyer.email,
        firstname: buyer.firstname,
        lastname: buyer.lastname,
        hash,
        role: 'BUYER',
      },
    });
  }

  console.log(' Seed data inserted successfully!');
}

main()
  .catch((e) => {
    console.error(' Error during seed:', e);
    process.exit(1);
  })
  .finally(async () => {
    await prisma.$disconnect();
  });

